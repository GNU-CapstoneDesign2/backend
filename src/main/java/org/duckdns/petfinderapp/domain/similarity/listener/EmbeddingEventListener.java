package org.duckdns.petfinderapp.domain.similarity.listener;

import org.duckdns.petfinderapp.domain.similarity.dto.event.EmbeddingCompleteEvent;
import org.duckdns.petfinderapp.domain.similarity.service.EmbeddingService;
import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmbeddingEventListener {
	private final EmbeddingService embeddingService;

	@SqsListener(value = {"https://sqs.ap-northeast-2.amazonaws.com/074682456017/embed-complete-response.fifo"})
	public void handleEmbeddingComplete(EmbeddingCompleteEvent event) {
		// 임베딩 완료 시 유사도 연산
		embeddingService.handleEmbeddingComplete(event);
	}
}
