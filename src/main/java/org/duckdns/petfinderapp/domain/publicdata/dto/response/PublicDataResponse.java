package org.duckdns.petfinderapp.domain.publicdata.dto.response;

public record PublicDataResponse<T>(
	PublicDataHeaderResponse header,
	PublicDataBodyResponse<T> body
) {
}
