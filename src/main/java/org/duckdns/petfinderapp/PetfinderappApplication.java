package org.duckdns.petfinderapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PetfinderappApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetfinderappApplication.class, args);
	}

}
