package org.duckdns.petfinderapp.domain.map.dto.request;

import jakarta.validation.constraints.AssertTrue;
import java.util.List;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.springframework.web.bind.annotation.RequestParam;

public record MapPostRequest(
    @RequestParam double minLat,
    @RequestParam double maxLat,
    @RequestParam double minLng,
    @RequestParam double maxLng,
    @RequestParam(required = false) List<PostState> states,
    @RequestParam(required = false) List<PetType> species
){
  @AssertTrue(message = "coordinate가 유효하지 않습니다")
  public boolean isValidCoordinates() {
    // 최소 위도(minLat)는 최대 위도(maxLat)보다 커야 하고,
    // 최소 경도(minLng)와 최대 경도(maxLng)보다 커야 합니다.
    if (minLat > maxLat || minLng > maxLng) {
      return false;
    }
    // 위도는 -90도에서 90도 사이, 경도는 -180도에서 180도 사이여야 합니다.
    if (minLat < -90 || maxLat > 90 || minLng < -180 || maxLng > 180) {
      return false;
    }
    return true;
  }
}
