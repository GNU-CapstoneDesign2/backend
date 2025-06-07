package org.duckdns.petfinderapp.domain.publicdata.dto.response;

public record PublicDataHeaderResponse(
	String reqNo,
	String resultCode,
	String resultMsg,
	String errorMsg
) {
}
