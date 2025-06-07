package org.duckdns.petfinderapp.domain.publicdata.client;

import org.duckdns.petfinderapp.domain.publicdata.config.SgisProperties;
import org.duckdns.petfinderapp.domain.publicdata.dto.request.GeocodingApiRequest;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisAccessTokenResult;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisGeocodingResult;
import org.duckdns.petfinderapp.domain.publicdata.dto.response.SgisResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SgisClient {
	private final SgisProperties sgisProperties;
	private final WebClient sgisApiWebClient;

	public Mono<SgisResponse<SgisGeocodingResult>> fetchCoordinatesByAddress(GeocodingApiRequest request) {
		return sgisApiWebClient.get()
			.uri(uriBuilder ->
				uriBuilder
					.path(sgisProperties.geocoding().uri())
					.queryParam("accessToken", request.accessToken())
					.queryParam("address", request.address())
					.build()
			)
			.exchangeToMono(resp -> {
				if (resp.statusCode().is2xxSuccessful()) {
					return resp.bodyToMono(new ParameterizedTypeReference<>() {
					});
				} else {
					return resp.bodyToMono(String.class)
						.flatMap(body -> {
							log.error("SGIS API 오류 응답: status={} body={}", resp.statusCode(), body);
							return Mono.error(new RuntimeException("SGIS API 요청 실패: " + resp.statusCode()));
						});
				}
			});
	}

	public Mono<SgisResponse<SgisAccessTokenResult>> fetchSgisAccessToken() {
		return sgisApiWebClient.get()
			.uri(uriBuilder ->
				uriBuilder
					.path(sgisProperties.auth().uri())
					.queryParam("consumer_key", sgisProperties.auth().key())
					.queryParam("consumer_secret", sgisProperties.auth().secret())
					.build()
			)
			.exchangeToMono(resp -> {
				if (resp.statusCode().is2xxSuccessful()) {
					return resp.bodyToMono(new ParameterizedTypeReference<>() {
					});
				} else {
					return resp.bodyToMono(String.class)
						.flatMap(body -> {
							log.error("SGIS API 오류 응답: status={} body={}", resp.statusCode(), body);
							return Mono.error(new RuntimeException("SGIS API 요청 실패: " + resp.statusCode()));
						});
				}
			});
	}
}
