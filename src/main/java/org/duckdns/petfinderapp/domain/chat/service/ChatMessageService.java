package org.duckdns.petfinderapp.domain.chat.service;

import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageService {
    Page<ChatMessageDto> getChatRoomMessages(User user, Long roomId, Pageable pageable);
}
