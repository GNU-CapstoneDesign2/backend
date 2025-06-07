package org.duckdns.petfinderapp.domain.publicdata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sgis")
public record SgisProperties(
	String baseUrl,
	Geocoding geocoding,
	Auth auth
) {
	public record Geocoding(
		String uri
	) {
	}

	public record Auth(
		String uri,
		String key,
		String secret
	) {
	}
}
