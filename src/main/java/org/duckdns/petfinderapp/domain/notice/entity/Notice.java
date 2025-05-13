package org.duckdns.petfinderapp.domain.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.duckdns.petfinderapp.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_seq")
    @SequenceGenerator(
            name = "notice_seq",
            sequenceName = "notice_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "create_at",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(name = "image_id", nullable = false)
    private Integer imageId;

    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "read", nullable = false)
    private Boolean read;
}