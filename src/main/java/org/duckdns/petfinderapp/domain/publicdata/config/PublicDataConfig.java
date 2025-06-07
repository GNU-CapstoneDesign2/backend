package org.duckdns.petfinderapp.domain.publicdata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PublicDataConfig {

	private static final int MAX_BUFFER_SIZE = 2 * 1024 * 1024; // 2MB
	private final SgisProperties sgisProperties;
	private final PublicDataProperties publicDataProperties;

	/**
	 * PublicData(유기동물 조회 등) 전용 WebClient
	 */
	@Bean
	public WebClient publicDataApiWebClient(WebClient.Builder builder) {
		DefaultUriBuilderFactory factory =
			new DefaultUriBuilderFactory(publicDataProperties.baseUrl());
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

		return builder
			.uriBuilderFactory(factory)
			.baseUrl(publicDataProperties.baseUrl())
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer ->
					configurer.defaultCodecs()
						.maxInMemorySize(MAX_BUFFER_SIZE)
				)
				.build()
			)
			.build();
	}

	/**
	 * SGIS API 전용 WebClient
	 */
	@Bean
	public WebClient sgisApiWebClient(WebClient.Builder builder) {
		return builder
			.baseUrl(sgisProperties.baseUrl())
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer ->
					configurer.defaultCodecs()
						.maxInMemorySize(MAX_BUFFER_SIZE)
				)
				.build()
			)
			.build();
	}
}
