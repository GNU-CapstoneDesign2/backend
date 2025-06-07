package org.duckdns.petfinderapp.domain.publicdata.dto.response;

public record SgisAccessTokenResult(
	String accessToken,
	Long accessTimeout
) {
}
