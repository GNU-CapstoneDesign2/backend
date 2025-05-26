package org.duckdns.petfinderapp.domain.post.dto;

import lombok.Builder;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PostState;

import java.time.LocalDateTime;

@Builder
public record PostInfoDto (
	Long postId,
	PostState state,
	LocalDateTime date,
	String address,
	String description,
	String imageUrl
) {

    public static PostInfoDto of(PostCommon post) {
		return PostInfoDto.builder()
			.postId(post.getId())
			.state(post.getState())
			.date(post.getCreateAt())
			.address(post.getAddress())
			.description(post.getDescription())
			.imageUrl(post.getImages().get(0).getImageUrl())
			.build();
    }
}
