package org.duckdns.petfinderapp.domain.chat.repository;

import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
