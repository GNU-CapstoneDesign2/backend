package org.duckdns.petfinderapp.domain.chat.controller;

import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.dto.response.ReadMessageDto;
import org.duckdns.petfinderapp.domain.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
	private final ChatService chatService;

	@Transactional
	@MessageMapping("/chatrooms/{roomId}")
	public void handleChatMessage(
		@DestinationVariable Long roomId,
		@Payload @Valid ChatMessageDto messageDto
	) {
		chatService.processMessage(roomId, messageDto);
	}

	@Transactional
	@MessageMapping("/chatrooms/{roomId}/read")
	public void handleReadMessage(
			@DestinationVariable Long roomId,
			@Payload @Valid ReadMessageDto readMessageDto
	) {
		chatService.processReadMessage(roomId, readMessageDto);
	}
}
