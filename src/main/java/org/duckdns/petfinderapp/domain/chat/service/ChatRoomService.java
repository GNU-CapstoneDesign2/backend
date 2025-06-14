package org.duckdns.petfinderapp.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.dto.ChatRoomDto;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomDto> getChatRoomList(Long userId) {
        // DB에서 해당 사용자가 참여한 채팅방들을 가져온다
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserIdOrderByLatest(userId);
        List<ChatRoomDto> result = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            // 현재 사용자가 sender면 상대방은 receiver, 반대면 sender
            User otherUser;
            if (chatRoom.getSender().getId().equals(userId)) {
                otherUser = chatRoom.getReceiver();
            } else {
                otherUser = chatRoom.getSender();
            }

            // 마지막 메시지 찾기
            String lastMessage;
            LocalDateTime lastMessageTime;

            if (chatRoom.getMessages().isEmpty()) {
                // 메시지가 하나도 없으면
                lastMessage = "대화를 시작해보세요";
                lastMessageTime = chatRoom.getCreateAt();
            } else {
                ChatMessage lastChatMessage = chatRoom.getMessages().get(chatRoom.getMessages().size() - 1);
                lastMessage = lastChatMessage.getContent();
                lastMessageTime = lastChatMessage.getCreateAt();
            }

            ChatRoomDto response = ChatRoomDto.builder()
                    .roomId(chatRoom.getId())
                    .senderName(otherUser.getName())
                    .senderProfile(otherUser.getImageUrl())
                    .lastMessage(lastMessage)
                    .lastMessageTime(lastMessageTime)
                    .build();

            result.add(response);
        }

        return result;
    }
}
