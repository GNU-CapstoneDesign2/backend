package org.duckdns.petfinderapp.domain.map.service;

import org.duckdns.petfinderapp.domain.map.dto.request.MapMarkerRequest;
import org.duckdns.petfinderapp.domain.map.dto.request.MapPostRequest;
import org.duckdns.petfinderapp.domain.map.dto.response.MapMarkerResponse;
import org.duckdns.petfinderapp.domain.post.dto.response.PostSummaryResponse;
import org.duckdns.petfinderapp.domain.map.dto.response.MapSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MapService {

  Page<MapSearchResponse> mapSearch(String query, Pageable pageable);

  Page<PostSummaryResponse> getPostsByCoordinates(
      MapPostRequest mapPostRequest,
      Pageable pageable);

  MapMarkerResponse getMarkersByCoordinates(MapMarkerRequest mapMarkerRequest);
}
