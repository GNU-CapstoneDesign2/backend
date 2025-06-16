package org.duckdns.petfinderapp.domain.similarity.dto.item;

public record ImageAiSimilarityItem(
	Long lostPostId,
	Long similarPostId,
	Double score
) {
}
