package org.duckdns.petfinderapp.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.post.dto.request.LostCreate;
import org.duckdns.petfinderapp.domain.post.dto.request.LostUpdate;
import org.duckdns.petfinderapp.domain.post.dto.response.ResLost;
import org.duckdns.petfinderapp.domain.post.service.LostService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LostController {
    private final LostService lostService;

    // lost 게시글 작성
    @PostMapping(value = "/posts/lost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long create(@RequestPart(value = "image", required = false) List<MultipartFile> image,
                       @RequestPart(value = "json") LostCreate requestDto,
                       @AuthenticationPrincipal User user) {
        return lostService.create(requestDto, image, user);
    }

    // lost 상세 조회
    @GetMapping("/posts/lost/{id}")
    public ResLost Lost(@PathVariable Long id) {
        return lostService.searchLost(id);
    }

    // lost 업데이트
    @PatchMapping(value = "/posts/lost/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long update(@PathVariable Long id,
                       @RequestPart(value = "json") LostUpdate requestDto,
                       @RequestPart(value = "image", required = false)List<MultipartFile> image,
                       @AuthenticationPrincipal User user) {
        return lostService.update(id, requestDto, image, user);
    }
}
