package org.duckdns.petfinderapp.domain.chat.service;

import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;

public interface ChatService {
	void processMessage(Long roomId, ChatMessageDto message);
}
