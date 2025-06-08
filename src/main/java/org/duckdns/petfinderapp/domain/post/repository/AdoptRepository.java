package org.duckdns.petfinderapp.domain.post.repository;

import java.util.List;

import org.duckdns.petfinderapp.domain.post.entity.Adopt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptRepository extends JpaRepository<Adopt, Long> {
	List<Adopt> findAllByDesertionNumIn(List<String> animalNums);
}
