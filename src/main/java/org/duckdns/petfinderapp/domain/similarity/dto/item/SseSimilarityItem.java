package org.duckdns.petfinderapp.domain.similarity.dto.item;

import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.similarity.entity.Similarity;

import lombok.Builder;

@Builder
public record SseSimilarityItem(
	Long similarPostId,
	String imageUrl,
	PostState postState,
	String date,
	String address,
	String description
) {
	public static SseSimilarityItem of(Similarity similarity) {
		return SseSimilarityItem.builder()
			.similarPostId(similarity.getSimilarPost().getId())
			.imageUrl(similarity.getSimilarPost().getImages().isEmpty() ? null :
				similarity.getSimilarPost().getImages().get(0).getFileURL())
			.postState(similarity.getSimilarPost().getState())
			.date(similarity.getSimilarPost().getDate().toString())
			.address(similarity.getSimilarPost().getAddress())
			.description(similarity.getSimilarPost().getContent())
			.build();
	}
}
