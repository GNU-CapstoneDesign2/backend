package org.duckdns.petfinderapp.domain.user.controller;

import org.duckdns.petfinderapp.domain.post.dto.response.PostSummaryResponse;
import org.duckdns.petfinderapp.domain.post.service.PostService;
import org.duckdns.petfinderapp.domain.user.dto.request.UserUpdateRequest;
import org.duckdns.petfinderapp.domain.user.dto.response.UserInfoResponse;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.service.UserService;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final PostService postService;

  @GetMapping("/me")
  public ApiResponse<UserInfoResponse> getCurrentUser(@AuthenticationPrincipal User user) {
    return ApiResponse.onSuccess(
        HttpStatus.OK,
        "내 정보 조회 성공",
        UserInfoResponse.of(user)
    );
  }

  @PutMapping("/me")
  public ApiResponse<UserInfoResponse> updateUser(
      @AuthenticationPrincipal User user,
      @Validated @RequestBody UserUpdateRequest userUpdateRequest
  ) {
    UserInfoResponse data = userService.updateUserInfo(user, userUpdateRequest);
    return ApiResponse.onSuccess(
        HttpStatus.OK,
        "내 정보 수정 성공",
        data
    );
  }

  @DeleteMapping("/me")
  public ApiResponse<Void> deleteUser(
      @AuthenticationPrincipal User user
  ) {
    userService.deleteUser(user);
    return ApiResponse.onSuccess(HttpStatus.NO_CONTENT, "회원 탈퇴 성공", null);
  }

  @GetMapping("/me/posts")
  public ApiResponse<Page<PostSummaryResponse>> getMyPosts(
      @AuthenticationPrincipal User user,
      Pageable pageable) {
    Page<PostSummaryResponse> data = postService.getMyPosts(user, pageable);
    return ApiResponse.onSuccess(
        HttpStatus.OK,
        "내 게시글 조회 성공",
        data
    );
  }
}
