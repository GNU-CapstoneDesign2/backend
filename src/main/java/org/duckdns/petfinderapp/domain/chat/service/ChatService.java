package org.duckdns.petfinderapp.domain.chat.service;

import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.dto.response.ReadMessageDto;

public interface ChatService {
	void processMessage(Long roomId, ChatMessageDto message);

	void processReadMessage(Long roomId, ReadMessageDto readMessageDto);
}
