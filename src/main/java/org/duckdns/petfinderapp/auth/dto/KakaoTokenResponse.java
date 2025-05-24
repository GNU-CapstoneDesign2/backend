package org.duckdns.petfinderapp.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponse(
	@JsonProperty("access_token")
	String accessToken
) {
}
