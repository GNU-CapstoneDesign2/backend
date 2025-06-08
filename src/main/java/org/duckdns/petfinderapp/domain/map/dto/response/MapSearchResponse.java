package org.duckdns.petfinderapp.domain.map.dto.response;

import org.duckdns.petfinderapp.domain.post.entity.Adopt;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.entity.Lost;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;

import lombok.Builder;

@Builder
public record MapSearchResponse(
	Long id, // 게시글 ID
	String name, // 동물등록번호
	String address, // 주소
	Coordinates coordinates // 좌표
) {
	public static MapSearchResponse of(PostCommon postCommon) {
		String petNum = postCommon instanceof Adopt ?
			((Adopt)postCommon).getPetNum() : ((Lost)postCommon).getPetNum();

		return MapSearchResponse.builder()
			.id(postCommon.getId())
			.name(petNum)
			.address(postCommon.getAddress())
			.coordinates(postCommon.getCoordinates())
			.build();
	}
}
