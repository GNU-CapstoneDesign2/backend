package org.duckdns.petfinderapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "org.duckdns.petfinderapp.domain")
public class PetfinderappApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetfinderappApplication.class, args);
	}

}
