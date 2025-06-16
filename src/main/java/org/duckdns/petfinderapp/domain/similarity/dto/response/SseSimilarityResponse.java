package org.duckdns.petfinderapp.domain.similarity.dto.response;

import java.util.List;

import org.duckdns.petfinderapp.domain.similarity.dto.item.SseSimilarityItem;

import lombok.Builder;

@Builder
public record SseSimilarityResponse(
	List<SseSimilarityItem> items,
	Integer count
) {
	public static SseSimilarityResponse of(List<SseSimilarityItem> existing, int size) {
		return SseSimilarityResponse.builder()
			.items(existing)
			.count(size)
			.build();
	}
}
