package org.duckdns.petfinderapp.domain.push.repository;

import org.duckdns.petfinderapp.domain.push.entity.Push;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushRepository extends JpaRepository<Push, Long> {
    List<Push> findByUserOrderByCreatedAtDesc(User user);
}
