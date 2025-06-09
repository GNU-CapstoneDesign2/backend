package org.duckdns.petfinderapp.domain.map.service;

import org.duckdns.petfinderapp.domain.map.dto.request.MapPostRequest;
import org.duckdns.petfinderapp.domain.map.dto.response.MapPostsResponse;
import org.duckdns.petfinderapp.domain.map.dto.response.MapSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MapService {

  Page<MapSearchResponse> mapSearch(String query, Pageable pageable);

  Page<MapPostsResponse> getPostsByCoordinates(
      MapPostRequest mapPostRequest,
      Pageable pageable);
}
