package org.duckdns.petfinderapp.domain.chat.repository;

import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
