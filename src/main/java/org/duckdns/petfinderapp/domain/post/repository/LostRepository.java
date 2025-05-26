package org.duckdns.petfinderapp.domain.post.repository;

import org.duckdns.petfinderapp.domain.post.entity.Lost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostRepository extends JpaRepository<Lost, Long> {
}
