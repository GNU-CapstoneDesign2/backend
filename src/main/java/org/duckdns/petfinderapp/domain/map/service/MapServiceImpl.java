package org.duckdns.petfinderapp.domain.map.service;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.duckdns.petfinderapp.domain.map.dto.item.MarkerItem;
import org.duckdns.petfinderapp.domain.map.dto.request.MapMarkerRequest;
import org.duckdns.petfinderapp.domain.map.dto.request.MapPostRequest;
import org.duckdns.petfinderapp.domain.map.dto.response.MapMarkerResponse;
import org.duckdns.petfinderapp.domain.post.dto.response.PostSummaryResponse;
import org.duckdns.petfinderapp.domain.map.dto.response.MapSearchResponse;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

  private final PostRepository postRepository;

  @Override
  public Page<MapSearchResponse> mapSearch(String query, Pageable pageable) {
    return postRepository.findAdoptOrLostByPetNum(query, pageable)
        .map(MapSearchResponse::of);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostSummaryResponse> getPostsByCoordinates(
      MapPostRequest mapPostRequest,
      Pageable pageable) {

    // 1) states 또는 petTypes 가 null 이거나 비어있으면 -> 결과 없음
    if (mapPostRequest.states() == null || mapPostRequest.states().isEmpty()
        || mapPostRequest.petTypes() == null || mapPostRequest.petTypes().isEmpty()) {
      return Page.empty(pageable);
    }

    Specification<PostCommon> spec = ((root, query, criteriaBuilder) -> {
      Predicate latBetween = criteriaBuilder.between(
          root.get("coordinates").get("latitude"), mapPostRequest.minLat(),
          mapPostRequest.maxLat());
      Predicate lngBetween = criteriaBuilder.between(
          root.get("coordinates").get("longitude"), mapPostRequest.minLng(),
          mapPostRequest.maxLng());
      Predicate stateIn = root.get("state").in(mapPostRequest.states());
      Predicate petTypesIn = root.get("petType").in(mapPostRequest.petTypes());
      Predicate notEndPost = criteriaBuilder.notEqual(
          root.get("state"), PostState.END);

      return criteriaBuilder.and(latBetween, lngBetween, stateIn, petTypesIn, notEndPost);
    });

    return postRepository.findAll(spec, pageable)
        .map(PostSummaryResponse::of);
  }

  @Override
  @Transactional(readOnly = true)
  public MapMarkerResponse getMarkersByCoordinates(MapMarkerRequest mapMarkerRequest) {

    // 1) states 또는 petTypes 가 null 이거나 비어있으면 -> 결과 없음
    if (mapMarkerRequest.states() == null || mapMarkerRequest.states().isEmpty()
        || mapMarkerRequest.petTypes() == null || mapMarkerRequest.petTypes().isEmpty()) {
      return MapMarkerResponse.empty();
    }

    Specification<PostCommon> spec = ((root, query, criteriaBuilder) -> {
      Predicate latBetween = criteriaBuilder.between(
          root.get("coordinates").get("latitude"), mapMarkerRequest.minLat(),
          mapMarkerRequest.maxLat());
      Predicate lngBetween = criteriaBuilder.between(
          root.get("coordinates").get("longitude"), mapMarkerRequest.minLng(),
          mapMarkerRequest.maxLng());
      Predicate stateIn = root.get("state").in(mapMarkerRequest.states());
      Predicate petTypesIn = root.get("petType").in(mapMarkerRequest.petTypes());
      Predicate notEndPost = criteriaBuilder.notEqual(
          root.get("state"), PostState.END);

      return criteriaBuilder.and(latBetween, lngBetween, stateIn, petTypesIn, notEndPost);
    });

    List<MarkerItem> markerItemList = postRepository.findAll(spec).stream()
        .map(postCommon -> MarkerItem.of(postCommon, postCommon.getImages().get(0).getFileURL()))
        .toList();

    return MapMarkerResponse.from(markerItemList);
  }
}
