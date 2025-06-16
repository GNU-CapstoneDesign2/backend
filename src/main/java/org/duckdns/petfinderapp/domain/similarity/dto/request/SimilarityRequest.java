package org.duckdns.petfinderapp.domain.similarity.dto.request;

import org.duckdns.petfinderapp.domain.post.enums.PostState;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Builder;

@Builder
public record SimilarityRequest(
	@JsonProperty("post_id")
	@JsonSerialize(using = ToStringSerializer.class)
	Long postId,
	PostState state,
	@JsonProperty("image_url")
	String imageUrl
) {
	public static SimilarityRequest of(Long postId, PostState postState, String imageUrl) {
		return SimilarityRequest.builder()
			.postId(postId)
			.state(postState)
			.imageUrl(imageUrl)
			.build();
	}
}
