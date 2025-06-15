package org.duckdns.petfinderapp.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomDto {
    private Long roomId;
    private String senderName;
    private String senderProfile;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
}
