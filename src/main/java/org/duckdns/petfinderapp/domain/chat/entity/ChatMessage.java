package org.duckdns.petfinderapp.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.duckdns.petfinderapp.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_message_seq")
    @SequenceGenerator(
            name = "chat_message_seq",
            sequenceName = "chat_message_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "create_at",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    /** 속한 채팅방 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatRoom chatRoom;

    /** 메시지 전송자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /** 내용 */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** 읽음 여부 */
    @Column(nullable = false)
    private Boolean read;
}