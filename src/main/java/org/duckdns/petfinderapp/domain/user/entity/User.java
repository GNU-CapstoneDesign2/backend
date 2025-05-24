package org.duckdns.petfinderapp.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.duckdns.petfinderapp.domain.user.enums.UserStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(length = 50, nullable = false)
    private String provider;

    @Column(length = 100, nullable = false, unique = true)
    private String providerId;

    @Column(length = 50)
    private String name;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public void updateUserInfo(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void deactivate() {
        this.status = UserStatus.DEACTIVATE;
    }
}
