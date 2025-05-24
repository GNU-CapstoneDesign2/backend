package org.duckdns.petfinderapp.domain.user.dto.response;

import org.duckdns.petfinderapp.domain.user.entity.User;

import lombok.Builder;

@Builder
public record UserInfoResponse(
	String name,
	String image
) {
	public static UserInfoResponse of(User user) {
		return UserInfoResponse.builder()
			.name(user.getName())
			.image(user.getImageUrl())
			.build();
	}
}
