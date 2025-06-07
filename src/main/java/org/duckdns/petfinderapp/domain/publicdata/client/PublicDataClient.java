package org.duckdns.petfinderapp.domain.publicdata.client;

import java.util.Optional;

import org.duckdns.petfinderapp.domain.publicdata.config.PublicDataProperties;
import org.duckdns.petfinderapp.domain.publicdata.dto.request.AdoptApiRequest;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.AdoptApiResponse;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.ErrorResponseWrapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataClient {

	private final PublicDataProperties publicDataProperties;
	private final WebClient publicDataApiWebClient;

	public Mono<AdoptApiResponse> fetchAdopts(AdoptApiRequest request) {
		return publicDataApiWebClient.get()
			.uri(uriBuilder ->
				uriBuilder
					.path(publicDataProperties.adopt().uri()) // "/abandonmentPublicService_v2/abandonmentPublic_v2"
					.queryParam("serviceKey", request.serviceKey())
					.queryParam("_type", "json")
					.queryParam("numOfRows", request.numberOfRows())
					.queryParamIfPresent("bgupd", Optional.ofNullable(request.updateDate()))
					.queryParamIfPresent("state", Optional.ofNullable(request.state()))
					.build()
			)
			.exchangeToMono(resp -> handlePublicDataResponse(resp, AdoptApiResponse.class));
	}

	private <T> Mono<T> handlePublicDataResponse(
		ClientResponse response,
		Class<T> successDtoClass
	) {
		MediaType contentType = response.headers().contentType()
			.orElse(MediaType.APPLICATION_OCTET_STREAM);
		int statusCode = response.statusCode().value();

		// 1) XML 게이트웨이 오류
		if (contentType.isCompatibleWith(MediaType.APPLICATION_XML) ||
			contentType.isCompatibleWith(MediaType.TEXT_XML)) {
			return response.bodyToMono(String.class)
				.flatMap(rawXml -> {
					log.error("API 게이트웨이 오류 XML 응답: status={}, contentType={}, body={}",
						statusCode, contentType, rawXml);
					try {
						XmlMapper xmlMapper = new XmlMapper();
						ErrorResponseWrapper errorDto =
							xmlMapper.readValue(rawXml, ErrorResponseWrapper.class);

						int reasonCode = errorDto.cmmMsgHeader().returnReasonCode();
						String authMsg = errorDto.cmmMsgHeader().returnAuthMsg();
						String errMsg = errorDto.cmmMsgHeader().errMsg();

						return switch (reasonCode) {
							case 22, 23 -> {
								log.warn("요청 제한 초과({}). 잠시 뒤 재시도 필요.", authMsg);
								yield Mono.empty();
							}
							case 30 -> {
								log.error("서비스키 미등록 에러: {}. serviceKey 확인하세요.", authMsg);
								yield Mono.error(new RuntimeException("잘못된 서비스키: " + authMsg));
							}
							default -> {
								log.error("알 수 없는 게이트웨이 오류: code={}, msg={}", reasonCode, errMsg);
								yield Mono.error(new RuntimeException("API 게이트웨이 오류: " + errMsg));
							}
						};
					} catch (Exception e) {
						log.error("XML 파싱 중 예외 발생: {}", e.getMessage(), e);
						return Mono.error(new RuntimeException("XML 파싱 오류", e));
					}
				});
		}

		// 2) JSON 정상 응답
		if (contentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
			return response.bodyToMono(successDtoClass)
				.doOnNext(dto -> log.info("구조 동물 조회 API 응답 성공"))
				.doOnNext(dto -> log.debug("구조 동물 조회 API 전체 응답: {}", dto))
				.doOnError(err -> log.error("구조 동물 조회 API JSON 파싱 실패: {}", err.getMessage(), err));
		}

		// 3) 그 외 예외
		return response.bodyToMono(String.class)
			.flatMap(raw -> {
				log.error("알 수 없는 응답 형식: status={}, contentType={}, body={}",
					statusCode, contentType, raw);
				return Mono.error(new RuntimeException("알 수 없는 응답 형식: " + contentType));
			});
	}
}
