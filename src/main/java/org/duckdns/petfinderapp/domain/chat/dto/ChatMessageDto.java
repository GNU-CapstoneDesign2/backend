package org.duckdns.petfinderapp.domain.chat.dto;

import org.duckdns.petfinderapp.domain.post.dto.PostInfoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageDto(
	@NotNull
	Long senderId,
	@NotBlank
	String message,

	PostInfoDto post
) {
}
