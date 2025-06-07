package org.duckdns.petfinderapp.domain.publicdata.dto.request;

import lombok.Builder;

@Builder
public record GeocodingApiRequest(
	String accessToken, // String, 필수, 액세스토큰
	String address, // String, 필수, 검색주소
	Integer pagenum, // default: 0, 선택, 페이지
	Integer resultcount // min: 1 max: 50 (default: 5), 선택, 결과 수
) {

	public static GeocodingApiRequest of(String accessToken, String address) {
		return GeocodingApiRequest.builder()
			.accessToken(accessToken)
			.address(address)
			.build();
	}
}
