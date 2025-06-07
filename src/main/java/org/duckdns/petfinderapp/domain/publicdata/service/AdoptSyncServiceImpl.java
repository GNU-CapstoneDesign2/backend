package org.duckdns.petfinderapp.domain.publicdata.service;

import java.util.List;

import org.duckdns.petfinderapp.domain.post.entity.Adopt;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.post.service.PostService;
import org.duckdns.petfinderapp.domain.publicdata.client.PublicDataClient;
import org.duckdns.petfinderapp.domain.publicdata.client.SgisClient;
import org.duckdns.petfinderapp.domain.publicdata.config.PublicDataProperties;
import org.duckdns.petfinderapp.domain.publicdata.dto.request.AdoptApiRequest;
import org.duckdns.petfinderapp.domain.publicdata.dto.request.GeocodingApiRequest;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.AdoptApiResponse;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.AdoptItem;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.PublicDataBodyResponse;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisGeocodingItem;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisGeocodingResult;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdoptSyncServiceImpl implements AdoptSyncService {

	private static final int NEW_FETCH_SIZE = 100;
	private static final int FULL_FETCH_SIZE = 1000;
	private static final List<String> STATES = List.of("notice", "protect", "finish");

	private final PublicDataClient publicDataClient;
	private final PublicDataProperties publicDataProperties;
	private final SgisClient sgisClient;
	private final PostService postService;
	private final SgisTokenService sgisTokenService;

	@Override
	public Mono<Void> addNewAdopts() {
		return fetchAdoptItems(NEW_FETCH_SIZE)
			.flatMapSequential(this::convertToAdoptWithCoordinates)
			.collectList()
			.flatMap(this::saveAdopts);
	}

	@Override
	public Mono<Void> refreshAllAdopts() {
		return Flux.merge(
				STATES.stream()
					.map(this::fetchAdoptItemsByState)
					.toList()
			)
			.collectList()
			.flatMap(adopts -> {
				log.info("전체 입양 동물 데이터 갱신 완료 - 총 {}건", adopts.size());
				return saveAdopts(adopts);
			})
			.onErrorResume(e -> {
				log.error("전체 입양 동물 데이터 갱신 중 에러 발생", e);
				return Mono.empty();
			});
	}

	private Flux<Adopt> fetchAdoptItemsByState(String state) {
		return fetchAdoptItems(state, FULL_FETCH_SIZE)
			.doOnSubscribe(s -> log.info("상태 '{}' 공고 조회 시작 ({}건)", state, FULL_FETCH_SIZE))
			.doOnComplete(() -> log.info("상태 '{}' 공고 조회 완료", state))
			.onErrorResume(e -> {
				log.error("상태 '{}' 공고 조회 중 에러 발생", state, e);
				return Flux.empty();
			})
			.flatMapSequential(item -> {
				PostState postState = PostState.fromString(state);
				if (postState == null) {
					log.warn("알 수 없는 상태 '{}' -> 스킵 (desertionNo={})", state, item.desertionNo());
					return Mono.empty();
				}
				return convertToAdoptWithCoordinates(item, postState);
			});
	}

	private Flux<AdoptItem> fetchAdoptItems(int fetchSize) {
		AdoptApiRequest request = AdoptApiRequest.forLatest(
			publicDataProperties.adopt().key(), fetchSize
		);
		return publicDataClient.fetchAdopts(request)
			.flatMapMany(this::extractAdoptItems);
	}

	private Flux<AdoptItem> fetchAdoptItems(String state, int fetchSize) {
		AdoptApiRequest request = AdoptApiRequest.forState(
			publicDataProperties.adopt().key(), state, fetchSize
		);
		return publicDataClient.fetchAdopts(request)
			.flatMapMany(this::extractAdoptItems);
	}

	private Flux<AdoptItem> extractAdoptItems(AdoptApiResponse response) {
		try {
			PublicDataBodyResponse<AdoptItem> body = response.response().body();
			if (body == null || body.items() == null || body.items().item() == null) {
				return Flux.empty();
			}
			List<AdoptItem> items = body.items().item();
			return items.isEmpty() ? Flux.empty() : Flux.fromIterable(items);
		} catch (Exception e) {
			log.warn("입양 동물 데이터 추출 실패", e);
			return Flux.empty();
		}
	}

	private Mono<Adopt> convertToAdoptWithCoordinates(AdoptItem item) {
		return getCoordinatesForAddress(item)
			.map(item::toAdopt)
			.doOnNext(a -> log.debug("변환 완료: {}", item.desertionNo()))
			.onErrorResume(e -> {
				log.warn("좌표 변환 실패 - 번호: {}, 주소: {}", item.desertionNo(), item.happenPlace(), e);
				return Mono.empty();
			});
	}

	private Mono<Adopt> convertToAdoptWithCoordinates(AdoptItem item, PostState state) {
		return getCoordinatesForAddress(item)
			.map(coords -> item.toAdopt(state, coords))
			.doOnNext(a -> log.debug("변환 완료: {}", item.desertionNo()))
			.onErrorResume(e -> {
				log.warn("좌표 변환 실패 - 번호: {}, 주소: {}", item.desertionNo(), item.happenPlace(), e);
				return Mono.empty();
			});
	}

	private Mono<Coordinates> getCoordinatesForAddress(AdoptItem item) {
		return sgisTokenService.getAccessToken()
			.flatMap(token -> {
				Mono<Coordinates> byPlace = callGeocodingApi(token, item.happenPlace())
					.flatMap(this::extractCoordinates);
				String combined = item.orgNm() + ' ' + item.happenPlace();
				Mono<Coordinates> byOrg = callGeocodingApi(token, combined)
					.flatMap(this::extractCoordinates);
				return byPlace.switchIfEmpty(byOrg)
					.switchIfEmpty(Mono.defer(() -> {
						log.warn("지오코딩 실패: '{}' / '{}' 결과 없음", item.happenPlace(), combined);
						return Mono.empty();
					}));
			});
	}

	private Mono<SgisResponse<SgisGeocodingResult>> callGeocodingApi(String token, String address) {
		GeocodingApiRequest req = GeocodingApiRequest.of(token, address);
		return sgisClient.fetchCoordinatesByAddress(req)
			.doOnNext(resp -> log.debug("지오코딩 응답 - 주소: {}, 결과: {}", address, resp));
	}

	private Mono<Coordinates> extractCoordinates(SgisResponse<SgisGeocodingResult> resp) {
		try {
			SgisGeocodingResult result = resp.result();
			if (result == null || result.resultdata() == null || result.resultdata().isEmpty()) {
				return Mono.empty();
			}
			SgisGeocodingItem geo = result.resultdata().get(0);
			return Mono.just(geo.toCoordinates());
		} catch (Exception e) {
			log.warn("좌표 추출 실패", e);
			return Mono.empty();
		}
	}

	private Mono<Void> saveAdopts(List<Adopt> adopts) {
		if (adopts.isEmpty()) {
			log.info("저장할 데이터 없음");
			return Mono.empty();
		}
		log.info("입양 동물 데이터 저장 시작 - 총 {}건", adopts.size());
		return Mono.fromCallable(() -> postService.upsertAdopts(adopts))
			.publishOn(Schedulers.boundedElastic())
			.doOnSuccess(count -> log.info("저장 완료 - 추가된 {}건", count))
			.then();
	}
}
