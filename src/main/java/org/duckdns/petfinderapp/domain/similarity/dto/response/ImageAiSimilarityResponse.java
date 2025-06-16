package org.duckdns.petfinderapp.domain.similarity.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageAiSimilarityResponse(
	@JsonProperty("originalpostId")
	Long postId,
	Integer count,
	@JsonProperty("postIds")
	List<Long> postIdList
) {
}
