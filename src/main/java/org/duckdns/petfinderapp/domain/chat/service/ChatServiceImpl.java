package org.duckdns.petfinderapp.domain.chat.service;

import java.util.Optional;

import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.dto.response.ReadMessageDto;
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
    log.debug("[processMessage] 진입: roomId={}, messageDto={}", roomId, message);

    Optional<User> sender = userRepository.findById(message.senderId());
    log.debug("[processMessage] sender 조회: senderId={} → exists={}", message.senderId(),
        sender.isPresent());

    if (sender.isEmpty()) {
      log.error("User not found: " + message.senderId());
      return;
    }
    Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
    log.debug("[processMessage] chatRoom 조회: roomId={} → exists={}", roomId, chatRoom.isPresent());

    if (chatRoom.isEmpty()) {
      log.error("Chat room not found: " + roomId);
      return;
    }
    ChatRoom room = chatRoom.get();
    log.debug("  ↳ chatRoom found: id={}, sender={}, receiver={}",
        room.getId(), room.getSender().getId(), room.getReceiver().getId());

    User senderUser = sender.get();
    log.debug("  ↳ sender found: id={}, name={}", senderUser.getId(), senderUser.getName());

    String messageContent = message.message();

    PostCommon sharePost = null;
    if (message.post() != null) {
      try {
        sharePost = postRepository.findById(message.post().postId())
            .orElseThrow(PostNotFoundException::missingPostCommon);
        log.debug("  ↳ sharePost found: id={}", sharePost.getId());
      } catch (Exception e) {
        log.error("[warn] shared post lookup failed: {}", e.getMessage());
      }
    }

    ChatMessage saved = chatMessageRepository.save(
        ChatMessage.of(room, senderUser, messageContent, sharePost));
    log.info("  ↳ message saved: messageId={}", saved.getId());

    String destination = "/topic/chatrooms/" + roomId;
    log.info("  ↳ sending to destination={} payload={}", destination, message);
    // 5. 메시지 전송
    try {
      messagingTemplate.convertAndSend(destination, message);
      log.info("  ↳ message sent successfully");
    } catch (Exception e) {
      log.error("Failed to send message: {}", e.getMessage());
    }
  }

  @Override
  public void processReadMessage(Long roomId, ReadMessageDto message) {
    String destination = "/topic/chatrooms/" + roomId + "/read";
    // 1. DTO에서 값 꺼내기
    Long userId = message.userId();
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
