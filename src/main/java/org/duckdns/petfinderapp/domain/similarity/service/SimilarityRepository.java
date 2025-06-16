package org.duckdns.petfinderapp.domain.similarity.service;

import java.util.List;

import org.duckdns.petfinderapp.domain.similarity.entity.Similarity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimilarityRepository extends JpaRepository<Similarity, Long> {
	List<Similarity> findAllByLostPostId(Long postId);
}
