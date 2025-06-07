package org.duckdns.petfinderapp.domain.publicdata.service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import org.duckdns.petfinderapp.domain.publicdata.client.SgisClient;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisAccessTokenResult;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class SgisTokenService {

	private final SgisClient sgisClient;

	// 현재 메모리에 저장된 액세스 토큰과 만료 시각
	private final AtomicReference<String> currentToken = new AtomicReference<>(null);
	private final AtomicReference<Instant> tokenExpiryTime = new AtomicReference<>(Instant.EPOCH);

	/**
	 * 애플리케이션 시작 직후, 토큰을 한 번 미리 받아두기
	 */
	@PostConstruct
	public void initializeToken() {
		log.info("애플리케이션 시작: SGIS 액세스 토큰 최초 요청");
		refreshToken()
			.subscribeOn(Schedulers.boundedElastic())
			.doOnError(e -> log.error("초기 토큰 요청 실패", e))
			.subscribe();
	}

	/**
	 * 토큰을 SGIS API에서 가져와 currentToken과 tokenExpiryTime을 갱신한다.
	 */
	public Mono<Void> refreshToken() {
		return sgisClient.fetchSgisAccessToken()
			.flatMap((SgisResponse<SgisAccessTokenResult> resp) -> {
				// SGIS 응답 구조에 맞춰, 첫번째 아이템에서 실제 토큰 문자열을 꺼낸다고 가정
				SgisAccessTokenResult tokenItem = resp.result();
				String accessToken = tokenItem.accessToken();
				Instant expiresAt = Instant.ofEpochMilli(tokenItem.accessTimeout());

				currentToken.set(accessToken);
				tokenExpiryTime.set(expiresAt);

				log.info("SGIS 액세스 토큰 갱신 완료, 만료시간: {}", expiresAt);
				return Mono.empty();
			});
	}

	/**
	 * 현재 저장된 토큰을 반환. 토큰이 없거나 만료 임박(30초 이내)이면 즉시 refreshToken()을 실행하고 새 토큰을 반환.
	 */
	public Mono<String> getAccessToken() {
		String token = currentToken.get();
		Instant expiresAt = tokenExpiryTime.get();

		if (token == null || Instant.now().isAfter(expiresAt.minusSeconds(30))) {
			log.info("토큰 없음 또는 곧 만료({}) 예정. 새 토큰 조회", expiresAt);
			return refreshToken()
				.then(Mono.fromSupplier(currentToken::get));
		}

		return Mono.just(token);
	}
}
