package org.duckdns.petfinderapp.domain.chat.repository;

import java.util.List;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomInAndIsReadFalseAndSenderNot(List<ChatRoom> chatRoomList, User sender);

    Page<ChatMessage> findAllByChatRoomId(Long roomId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true " +
        "WHERE m.chatRoom.id = :chatRoomId " +
        "AND m.sender.id != :currentUserId " +  // 내가 보낸 메시지가 아닌 것만
        "AND m.createAt <= (SELECT msg.createAt FROM ChatMessage msg WHERE msg.id = :lastReadMessageId)")
    void updateIsReadByLastReadMessage(@Param("chatRoomId") Long chatRoomId,
        @Param("currentUserId") Long currentUserId,
        @Param("lastReadMessageId") Long lastReadMessageId);
}
