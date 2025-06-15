package org.duckdns.petfinderapp.domain.similarity.service;

import java.util.UUID;

import org.duckdns.petfinderapp.domain.similarity.client.ImageAiClient;
import org.duckdns.petfinderapp.domain.similarity.dto.event.EmbeddingCompleteEvent;
import org.duckdns.petfinderapp.domain.similarity.dto.request.ImageAiEmbeddingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

	private final SqsAsyncClient sqsAsyncClient;
	private final SimilarityService similarityService;
	private final ImageAiClient imageAiClient;
	private final ObjectMapper jacksonObjectMapper;

	@Value("${cloud.aws.sqs.embedding-lost-queue-name}")
	private String lostEmbeddingQueueName;
	@Value("${cloud.aws.sqs.embedding-sna-queue-name}")
	private String otherEmbeddingQueueName;


	public void sendLostEmbeddingRequest(ImageAiEmbeddingRequest request) {
		try {
			String jsonPayload = jacksonObjectMapper.writeValueAsString(request);
			log.info("Sending JSON payload: {}", jsonPayload);

			SendMessageRequest sendRequest = SendMessageRequest.builder()
				.queueUrl(lostEmbeddingQueueName)
				.messageBody(jsonPayload)
				.messageGroupId("lost-embedding-group")
				.messageDeduplicationId(UUID.randomUUID().toString())
				.build();

			SendMessageResponse response = sqsAsyncClient.sendMessage(sendRequest).join();
			log.info("Message sent successfully: {}", response.messageId());

		} catch (Exception e) {
			log.error("Failed to send lost embedding request", e);
		}
	}

	public void sendOtherEmbeddingRequest(ImageAiEmbeddingRequest request) {
		try {
			String jsonPayload = jacksonObjectMapper.writeValueAsString(request);
			log.info("Sending JSON payload: {}", jsonPayload);

			SendMessageRequest sendRequest = SendMessageRequest.builder()
				.queueUrl(otherEmbeddingQueueName)
				.messageBody(jsonPayload)
				.messageGroupId("other-embedding-group")
				.messageDeduplicationId(UUID.randomUUID().toString())
				.build();

			SendMessageResponse response = sqsAsyncClient.sendMessage(sendRequest).join();
			log.info("Message sent successfully: {}", response.messageId());

		} catch (Exception e) {
			log.error("Failed to send other embedding request", e);
		}
	}

	public void fetchEmbeddingLost(ImageAiEmbeddingRequest request) {
		imageAiClient.fetchEmbeddingLostRequest(request);
	}

	public void fetchEmbeddingOther(ImageAiEmbeddingRequest request) {
		imageAiClient.fetchEmbeddingOtherRequest(request);
	}

	public void handleEmbeddingComplete(EmbeddingCompleteEvent event) {
		log.info("Handling embedding complete for postId: {}, postState: {}", event.postId(), event.postState());
		similarityService.processSimilarity(event.postId(), event.postState());
	}
}
