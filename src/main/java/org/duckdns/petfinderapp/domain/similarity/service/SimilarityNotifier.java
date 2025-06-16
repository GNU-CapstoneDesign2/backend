package org.duckdns.petfinderapp.domain.similarity.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import lombok.extern.slf4j.Slf4j;
import org.duckdns.petfinderapp.domain.similarity.dto.response.SseSimilarityResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimilarityNotifier {
	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final TaskScheduler taskScheduler;

	public SseEmitter register(Long postId) {
		SseEmitter emitter = new SseEmitter(30_000L); // 30초 타임아웃 설정
		log.debug("SSE registered for postId: {}", postId);

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
			}, Duration.ofSeconds(20) // 20초 주기로 ping 이벤트 전송
		);

		Runnable cleanupTask = () -> cleanup(postId, future, emitter);

		emitter.onCompletion(cleanupTask);
		emitter.onTimeout(cleanupTask);
		emitter.onError(e -> {
			log.warn("SSE error for postId: {}", postId, e);
			cleanupTask.run();
		});

		try {
			emitter.send(SseEmitter.event()
					.name("ping")
					.data("keep-alive")
			);
		} catch (IOException e) {
			log.warn("Initial ping failed for postId: {}", postId, e);
			cleanupTask.run();
			return emitter;
		}

		emitters.put(postId, emitter);
		log.debug("SSE registered for postId: {}", postId);

		return emitter;
	}

	private void cleanup(Long postId, ScheduledFuture<?> future, SseEmitter emitter) {
		emitters.remove(postId);
		future.cancel(true);
		emitter.complete();
		log.debug("SSE cleaned up for postId: {}", postId);
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
