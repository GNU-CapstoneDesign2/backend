package org.duckdns.petfinderapp.domain.post.dto;

import java.time.LocalDateTime;

public record PostInfoDto (
	Long postId,
	String state,
	LocalDateTime date,
	String address,
	String description,
	String imageUrl
) {

}
