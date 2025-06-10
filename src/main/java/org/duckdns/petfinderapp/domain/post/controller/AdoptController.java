package org.duckdns.petfinderapp.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.post.dto.response.ResAdopt;
import org.duckdns.petfinderapp.domain.post.service.AdoptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdoptController {
    private final AdoptService adoptService;

    // Adopt 상세 조회
    @GetMapping("/posts/adopt/{id}")
    public ResAdopt Apodt(@PathVariable Long id) {
        return adoptService.searchAdopt(id);
    }
}
