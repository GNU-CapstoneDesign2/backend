package org.duckdns.petfinderapp.domain.chat.service;

import java.util.Optional;

import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.dto.resposne.ReadMessageDto;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomAccessDeniedException;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomNotFoundException;
import org.duckdns.petfinderapp.domain.chat.repository.ChatMessageRepository;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.exception.PostNotFoundException;
import org.duckdns.petfinderapp.domain.post.repository.PostRepository;
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
	private final PostRepository postRepository;

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

		PostCommon sharePost = postRepository.findById(message.post().postId())
				.orElseThrow(PostNotFoundException::missingPost);

		chatMessageRepository.save(ChatMessage.of(chatRoom.get(), sender.get(), messageContent, sharePost));

		messagingTemplate.convertAndSend(destination, message);
	}

	@Override
	public void processReadMessage(Long roomId, ReadMessageDto message) {
		String destination = "/topic/chatrooms/" + roomId + "/read";
		// 1. DTO에서 값 꺼내기
		Long userId  = message.userId();
		Long lastReadMessageId = message.lastReadMessageId();

		// 2. 채팅방 존재 & 권한 검증
		ChatRoom room = chatRoomRepository.findById(roomId)
				.orElseThrow(ChatRoomNotFoundException::missingChatRoom);

		User user = userRepository.getReferenceById(userId);
		if (!room.getSender().getId().equals(user.getId()) &&
				!room.getReceiver().getId().equals(user.getId())) {
			throw ChatRoomAccessDeniedException.accessDenied();
		}

		// 3. 읽음 처리 로직
		// lastReadMessageId 이전 메시지는 모두 읽음 처리
		chatMessageRepository.updateIsReadByLastReadMessage(roomId, userId, lastReadMessageId);

		// 4. 읽음 처리된 메시지 전송
		messagingTemplate.convertAndSend(destination, message);
	}
}
