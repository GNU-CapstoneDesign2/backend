package org.duckdns.petfinderapp.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.post.dto.response.ResAdopt;
import org.duckdns.petfinderapp.domain.post.entity.Adopt;
import org.duckdns.petfinderapp.domain.post.repository.AdoptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdoptService {
    private final AdoptRepository adoptRepository;

    @Transactional(readOnly = true)
    public ResAdopt searchAdopt(Long id) {
        Adopt adopt = adoptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        return new ResAdopt(adopt);
    }
}
