package org.duckdns.petfinderapp.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.post.dto.request.LostCreate;
import org.duckdns.petfinderapp.domain.post.dto.request.LostUpdate;
import org.duckdns.petfinderapp.domain.post.dto.response.ResLost;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.service.LostService;
import org.duckdns.petfinderapp.domain.similarity.dto.request.ImageAiEmbeddingRequest;
import org.duckdns.petfinderapp.domain.similarity.service.EmbeddingService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LostController {
    private final LostService lostService;
    private final UserRepository userRepository;
    private final EmbeddingService embeddingService;

    // lost 게시글 작성
    @PostMapping(value = "/posts/lost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long create(@RequestPart(value = "image", required = false) List<MultipartFile> image,
                       @RequestPart(value = "json") LostCreate requestDto,
                       @AuthenticationPrincipal User user) {
        User usert = userRepository.findById(1L).get();
        PostCommon savedPost = lostService.create(requestDto, image, usert);
        // 이미지 임베딩 요청 이벤트 발행
        embeddingService.sendLostEmbeddingRequest(ImageAiEmbeddingRequest.of(savedPost));
        return savedPost.getId();
    }

    // lost 상세 조회
    @GetMapping("/posts/lost/{id}")
    public ResLost getLost(@PathVariable Long id,
                           @AuthenticationPrincipal User user) {
        return lostService.searchLost(id, user);
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
