package org.duckdns.petfinderapp.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;

@Getter
public class ReqCoordinates {
    private double latitude;
    private double longitude;

    @Builder
    public ReqCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates toEntity() {
        return Coordinates.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
