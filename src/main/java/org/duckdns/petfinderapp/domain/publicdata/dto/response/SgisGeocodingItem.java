package org.duckdns.petfinderapp.domain.publicdata.dto.response;

import org.duckdns.petfinderapp.domain.post.entity.Coordinates;

public record SgisGeocodingItem(
	Double x,
	Double y
) {
	public Coordinates toCoordinates() {
		return Coordinates.builder()
			.latitude(y)
			.longitude(x)
			.build();
	}
}
