package org.duckdns.petfinderapp.domain.similarity.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.duckdns.petfinderapp.domain.similarity.dto.response.SseSimilarityResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SimilarityNotifier {
	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final TaskScheduler taskScheduler;

	public SseEmitter register(Long postId) {
		SseEmitter emitter = new SseEmitter(30L); // 30초 타임아웃 설정

		ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(
			() -> {
				try {
					emitter.send(SseEmitter.event()
						.name("ping")
						.data("keep-alive")
					);
				} catch (IOException e) {
					emitter.completeWithError(e);
				}
			}, Duration.ofSeconds(20) // 5분마다 ping 이벤트 전송
		);

		emitter.onCompletion(() -> {
			emitters.remove(postId);
			future.cancel(true);
		});
		emitter.onTimeout(() -> {
			emitters.remove(postId);
			future.cancel(true);
		});
		emitters.put(postId, emitter);

		return emitter;
	}

	public void notifyComplete(Long postId, SseSimilarityResponse response) {
		SseEmitter emitter = emitters.remove(postId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event()
					.name("complete")
					.data(response)
				);
				emitter.complete();
			} catch (IOException e) {
				emitter.completeWithError(e);
			}
		}
	}
}
