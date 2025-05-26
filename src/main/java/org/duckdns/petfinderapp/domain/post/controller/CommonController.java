package org.duckdns.petfinderapp.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.post.dto.request.CommonCreate;
import org.duckdns.petfinderapp.domain.post.dto.request.CommonUpdate;
import org.duckdns.petfinderapp.domain.post.dto.response.ResCommon;
import org.duckdns.petfinderapp.domain.post.service.PostService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonController {
    private final PostService commonService;

    // found 게시글 작성
    @PostMapping(value = "/posts/found", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long create(@RequestPart(value = "image", required = false)List<MultipartFile> image,
                       @RequestPart(value = "json") CommonCreate requestDto,
                       @AuthenticationPrincipal User user) {
        return commonService.create(requestDto, image, user);
    }

    // Found 상세 조회
    @GetMapping("/posts/found/{id}")
    public ResCommon Found(@PathVariable Long id) {
        return commonService.searchFound(id);
    }

    // found 게시글 수정
    @PatchMapping(value = "/posts/found/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long update(@PathVariable Long id,
                       @RequestPart(value = "json") CommonUpdate requestDto,
                       @RequestPart(value = "image", required = false)List<MultipartFile> image,
                       @AuthenticationPrincipal User user) {
        return commonService.update(id, requestDto, image, user);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        commonService.delete(id, user);
    }
}
