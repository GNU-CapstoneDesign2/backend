package org.duckdns.petfinderapp.domain.publicdata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "public-data")
public record PublicDataProperties(
	String baseUrl,
	ApiProperties adopt
) {

	public record ApiProperties(
		String uri,
		String key
	) {
	}
}
