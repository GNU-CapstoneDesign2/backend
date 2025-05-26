package org.duckdns.petfinderapp.domain.push.repository;

import org.duckdns.petfinderapp.domain.push.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {
    Optional<FcmToken> findByUserId(Long userId);
}
