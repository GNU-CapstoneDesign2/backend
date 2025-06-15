package org.duckdns.petfinderapp.domain.similarity.client;

import java.util.List;
import org.duckdns.petfinderapp.domain.similarity.dto.request.ImageAiEmbeddingRequest;
import org.duckdns.petfinderapp.domain.similarity.dto.request.SimilarityRequest;
import org.duckdns.petfinderapp.domain.similarity.dto.response.ImageAiSimilarityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class ImageAiClient {
	private final WebClient webClient;

	public ImageAiClient(@Value("${image-ai.base-url}") String baseUrl) {
		this.webClient = WebClient.builder()
			.baseUrl(baseUrl)
			.build();
	}

	public ImageAiSimilarityResponse fetchSimilarOtherPosts(SimilarityRequest similarityRequest) {
		return webClient.post()
			.uri(uriBuilder -> uriBuilder.path("/find_sighting_with_missing_forNewMissing").build())
			.body(Mono.just(similarityRequest), SimilarityRequest.class)
			.exchangeToMono(this::handleResponse)
			.block();
	}

	public ImageAiSimilarityResponse fetchSimilarLostPosts(SimilarityRequest similarityRequest) {
		//TODO: 테스트용 모킹
		return new ImageAiSimilarityResponse(1L, 3, List.of(1L, 2L, 3L));

//		return webClient.post()
//			.uri(uriBuilder ->uriBuilder.path("/find_missing_with_sighting_forAlarm").build())
//			.body(Mono.just(similarityRequest), SimilarityRequest.class)
//			.exchangeToMono(this::handleResponse)
//			.block();
	}

	public void fetchEmbeddingLostRequest(ImageAiEmbeddingRequest request) {
		webClient.post()
			.uri(uriBuilder -> uriBuilder.path("/missing-posts").build())
			.body(Mono.just(request), ImageAiEmbeddingRequest.class)
			.exchangeToMono(this::handleResponse)
			.block();
	}

	public void fetchEmbeddingOtherRequest(ImageAiEmbeddingRequest request) {
		webClient.post()
			.uri(uriBuilder -> uriBuilder.path("/sighting-posts").build())
			.body(Mono.just(request), ImageAiEmbeddingRequest.class)
			.exchangeToMono(this::handleResponse)
			.block();
	}

	private Mono<ImageAiSimilarityResponse> handleResponse(ClientResponse resp) {
		if (resp.statusCode().is2xxSuccessful()) {
			return resp.bodyToMono(ImageAiSimilarityResponse.class);
		} else {
			return resp.bodyToMono(String.class)
				.flatMap(errorBody -> Mono.error(new RuntimeException("API 요청 실패: " + errorBody)));
		}
	}
}
