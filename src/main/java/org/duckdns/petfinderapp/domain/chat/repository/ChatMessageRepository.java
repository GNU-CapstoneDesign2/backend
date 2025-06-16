package org.duckdns.petfinderapp.domain.chat.repository;

import java.util.List;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomInAndIsReadFalseAndSenderNot(List<ChatRoom> chatRoomList, User sender);

    Page<ChatMessage> findAllByChatRoomId(Long roomId, Pageable pageable);
}
