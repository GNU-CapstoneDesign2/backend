package org.duckdns.petfinderapp.domain.post.repository;

import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostCommon, Long> {
    List<PostCommon> findAllByOrderByIdDesc();
}
