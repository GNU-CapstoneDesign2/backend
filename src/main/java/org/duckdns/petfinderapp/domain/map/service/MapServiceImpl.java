package org.duckdns.petfinderapp.domain.map.service;

import org.duckdns.petfinderapp.domain.map.dto.response.MapSearchResponse;
import org.duckdns.petfinderapp.domain.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
	private final PostRepository postRepository;

	@Override
	public Page<MapSearchResponse> mapSearch(String query, Pageable pageable) {
		return postRepository.findAdoptOrLostByPetNum(query, pageable)
			.map(MapSearchResponse::of);
	}
}
