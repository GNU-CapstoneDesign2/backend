package org.duckdns.petfinderapp.domain.chat.service;

import java.util.Optional;

import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.repository.ChatMessageRepository;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatMessageRepository chatMessageRepository;
	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;

	@Override
	public void processMessage(Long roomId, ChatMessageDto message) {
		String destination = "/topic/chatrooms/" + roomId;
		Optional<User> sender = userRepository.findById(message.senderId());
		if (sender.isEmpty()) {
		 log.error("User not found: " + message.senderId());
		 return;
		}
		Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
		if (chatRoom.isEmpty()) {
			log.error("Chat room not found: " + roomId);
			return;
		}
		String messageContent = message.message();
		chatMessageRepository.save(ChatMessage.of(chatRoom.get(), sender.get(), messageContent));

		messagingTemplate.convertAndSend(destination, message);
	}
}
