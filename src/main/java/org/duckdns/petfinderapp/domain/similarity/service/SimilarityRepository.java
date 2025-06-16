package org.duckdns.petfinderapp.domain.similarity.service;

import java.util.List;

import org.duckdns.petfinderapp.domain.similarity.entity.Similarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SimilarityRepository extends JpaRepository<Similarity, Long> {

  @Query("""
        SELECT s FROM Similarity s
         JOIN FETCH s.similarPost sp
         LEFT JOIN FETCH sp.images
        WHERE s.lostPost.id = :postId
      """)
  List<Similarity> findAllWithImagesByLostPostId(@Param("postId") Long postId);
}

