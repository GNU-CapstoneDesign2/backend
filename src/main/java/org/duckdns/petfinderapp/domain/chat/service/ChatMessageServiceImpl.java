package org.duckdns.petfinderapp.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomAccessDeniedException;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomNotFoundException;
import org.duckdns.petfinderapp.domain.chat.repository.ChatMessageRepository;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.post.dto.PostInfoDto;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Page<ChatMessageDto> getChatRoomMessages(User user, Long roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(ChatRoomNotFoundException::missingChatRoom);

        if (!chatRoom.getSender().equals(user) && !chatRoom.getReceiver().equals(user)) {
            throw ChatRoomAccessDeniedException.accessDenied();
        }

        PostInfoDto postInfoDto = PostInfoDto.of(chatRoom.getPost());
        Page<ChatMessage> chatMessagesPage = chatMessageRepository.findAllByChatRoomId(roomId, pageable);

        return chatMessagesPage.map(message -> ChatMessageDto.of(message, postInfoDto));
    }
}
