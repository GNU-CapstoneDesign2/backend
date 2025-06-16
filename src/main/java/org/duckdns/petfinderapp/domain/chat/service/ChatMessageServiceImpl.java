package org.duckdns.petfinderapp.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomAccessDeniedException;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomNotFoundException;
import org.duckdns.petfinderapp.domain.chat.repository.ChatMessageRepository;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> getChatRoomMessages(User user, Long roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(ChatRoomNotFoundException::missingChatRoom);

        if (!chatRoom.getSender().getId().equals(user.getId()) &&
            !chatRoom.getReceiver().getId().equals(user.getId())) {
            throw ChatRoomAccessDeniedException.accessDenied();
        }

        Page<ChatMessage> chatMessagesPage = chatMessageRepository.findAllByChatRoomId(roomId, pageable);

        return chatMessagesPage.map(ChatMessageDto::of);
    }
}
