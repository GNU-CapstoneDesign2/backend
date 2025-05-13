package org.duckdns.petfinderapp.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class PostCommon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_common_seq")
    @SequenceGenerator(
            name = "post_common_seq",
            sequenceName = "post_common_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "create_at",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(insertable = false, updatable = false)
    private String dtype;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(columnDefinition = "POINT", nullable = false)
    private String coordinates;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type", nullable = false)
    private PetType petType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostState state;
}
