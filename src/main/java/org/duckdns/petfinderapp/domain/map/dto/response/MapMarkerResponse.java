package org.duckdns.petfinderapp.domain.map.dto.response;

import java.util.List;
import org.duckdns.petfinderapp.domain.map.dto.item.MarkerItem;

public record MapMarkerResponse(
    List<MarkerItem> markers
) {


  public static MapMarkerResponse empty() {
    return new MapMarkerResponse(List.of());
  }

  public static MapMarkerResponse from(List<MarkerItem> markerItemList) {
    return new MapMarkerResponse(markerItemList);
  }
}
