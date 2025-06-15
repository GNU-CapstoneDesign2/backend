package org.duckdns.petfinderapp.domain.chat.repository;

import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 특정 사용자가 참여한 채팅방 목록 조회 (최신 메시지 시간 순)
    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.messages m " +
            "WHERE (cr.sender.id = :userId OR cr.receiver.id = :userId) " +
            "ORDER BY cr.createAt DESC")
    List<ChatRoom> findChatRoomsByUserIdOrderByLatest(@Param("userId") Long userId);

    // 게시글 ID에 대한 유저 ID 조회
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.post.id = :postId AND " +
            "(cr.sender.id = :userId OR cr.receiver.id = :userId)")
    Optional<ChatRoom> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

  	Optional<Object> findChatRoomByPostId(Long postId);
}
