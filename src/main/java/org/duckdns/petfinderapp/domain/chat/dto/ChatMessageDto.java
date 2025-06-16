package org.duckdns.petfinderapp.domain.chat.dto;

import lombok.Builder;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.post.dto.PostInfoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Builder
public record ChatMessageDto(
	Long messageId,
	@NotNull
	Long senderId,
	@NotBlank
	String message,
	LocalDateTime createAt,
	PostInfoDto post,
	Boolean isRead
) {
	public static ChatMessageDto of(ChatMessage chatMessage) {
		return ChatMessageDto.builder()
			.messageId(chatMessage.getId())
			.senderId(chatMessage.getSender().getId())
			.message(chatMessage.getContent())
			.createAt(chatMessage.getCreateAt())
			.post(chatMessage.getSharePost() != null ? PostInfoDto.of(chatMessage.getSharePost()) : null)
			.isRead(chatMessage.getIsRead())
			.build();
	}
}
