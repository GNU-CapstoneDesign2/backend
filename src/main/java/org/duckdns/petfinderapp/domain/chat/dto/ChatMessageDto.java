package org.duckdns.petfinderapp.domain.chat.dto;

import lombok.Builder;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.post.dto.PostInfoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Builder
public record ChatMessageDto(
	@NotNull
	Long senderId,
	@NotBlank
	String message,
	LocalDateTime createAt,
	PostInfoDto post,
	Boolean isRead
) {
	public static ChatMessageDto of(ChatMessage chatMessage, PostInfoDto postInfoDto) {
		return ChatMessageDto.builder()
			.senderId(chatMessage.getSender().getId())
			.message(chatMessage.getContent())
			.createAt(chatMessage.getCreateAt())
			.post(postInfoDto)
			.isRead(chatMessage.getRead())
			.build();
	}
}
