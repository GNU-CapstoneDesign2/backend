package org.duckdns.petfinderapp.domain.publicdata.dto.response;

public record PublicDataBodyResponse<T>(
	ItemWrapper<T> items,
	Integer numOfRows,
	Integer pageNo,
	Integer totalCount
) {
}
