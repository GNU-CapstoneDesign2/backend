package org.duckdns.petfinderapp.domain.push.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private boolean read;
    private Long userId;
    private String token;
    private LocalDateTime updatedAt;

    public FcmToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateToken(String token) {
        this.token = token;
        this.updatedAt = LocalDateTime.now();
    }
}
