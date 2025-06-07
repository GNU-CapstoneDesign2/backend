package org.duckdns.petfinderapp.domain.publicdata.dto.response;

import lombok.Builder;

@Builder
public record AdoptApiResponse(
	PublicDataResponse<AdoptItem> response
) {
}
