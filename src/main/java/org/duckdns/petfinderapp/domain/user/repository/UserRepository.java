package org.duckdns.petfinderapp.domain.user.repository;

import java.util.Optional;

import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByProviderId(String providerId);

	@Modifying
	@Query(
		value = "UPDATE users SET status = 'ACTIVATE' WHERE provider_id = :pid",
		nativeQuery = true
	)
	int activateByProviderId(String pid);
}
