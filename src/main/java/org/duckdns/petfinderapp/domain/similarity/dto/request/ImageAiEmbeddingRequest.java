package org.duckdns.petfinderapp.domain.similarity.dto.request;

import org.duckdns.petfinderapp.domain.post.entity.PostCommon;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record ImageAiEmbeddingRequest(
	@JsonProperty("post_id")
	Long postId,
	@JsonProperty("image_url")
	String imageUrl
) {
	public static ImageAiEmbeddingRequest of(PostCommon savedPost) {
		return ImageAiEmbeddingRequest.builder()
			.postId(savedPost.getId())
			.imageUrl(savedPost.getImages().isEmpty() ? null : savedPost.getImages().get(0).getFileURL())
			.build();
	}

	public static ImageAiEmbeddingRequest of(Long postId, PostCommon savedPost) {
		return ImageAiEmbeddingRequest.builder()
			.postId(postId)
			.imageUrl(savedPost.getImages().isEmpty() ? null : savedPost.getImages().get(0).getFileURL())
			.build();
	}
}
