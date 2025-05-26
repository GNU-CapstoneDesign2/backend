package org.duckdns.petfinderapp.domain.post.dto.response;

import lombok.Getter;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;

@Getter
public class ResCoordinates {
    private double latitude;
    private double longitude;

    public ResCoordinates(Coordinates coordinates) {
        this.latitude = coordinates.getLatitude();
        this.longitude = coordinates.getLongitude();
    }
}
