package org.duckdns.petfinderapp.domain.publicdata.dto.response;

public record SgisResponse<T>(
	String id,
	T result,
	String errMsg,
	Integer errCd,
	String trId
) {
}
