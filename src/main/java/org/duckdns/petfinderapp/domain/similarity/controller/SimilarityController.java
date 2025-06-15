package org.duckdns.petfinderapp.domain.similarity.controller;

import org.duckdns.petfinderapp.domain.similarity.service.SimilarityService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SimilarityController {

	private final SimilarityService similarityService;

	@GetMapping(value = "/similarity", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter streamSimilar(@RequestParam Long postId) {
		// 비즈니스 로직은 모두 서비스로 위임
		return similarityService.subscribeToSimilarity(postId);
	}
}
