package org.duckdns.petfinderapp.domain.post.repository;

import java.util.List;

import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostCommon, Long> {
    List<PostCommon> findAllByOrderByIdDesc();
}
