package org.duckdns.petfinderapp.domain.map.entity;

import jakarta.persistence.*;
import lombok.*;
import org.duckdns.petfinderapp.domain.map.enums.SearchType;
import org.duckdns.petfinderapp.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "search_history_seq")
    @SequenceGenerator(
            name = "search_history_seq",
            sequenceName = "search_history_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(name = "search_type", nullable = false)
    private SearchType searchType;

    @Column(name = "search_at",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime searchAt;
}

