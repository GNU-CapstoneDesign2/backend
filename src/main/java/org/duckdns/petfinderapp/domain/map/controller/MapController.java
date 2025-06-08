package org.duckdns.petfinderapp.domain.map.controller;

import org.duckdns.petfinderapp.domain.map.dto.response.MapSearchResponse;
import org.duckdns.petfinderapp.domain.map.service.MapService;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {
	private final MapService mapService;

	@GetMapping("/search")
	public ApiResponse<Page<MapSearchResponse>> mapSearch(@RequestParam String query, Pageable pageable) {
		Page<MapSearchResponse> data = mapService.mapSearch(query, pageable);
		return ApiResponse.onSuccess(HttpStatus.OK, "검색 성공", data);
	}
}
