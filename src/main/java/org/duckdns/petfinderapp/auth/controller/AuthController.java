package org.duckdns.petfinderapp.auth.controller;

import org.duckdns.petfinderapp.auth.dto.LoginResponse;
import org.duckdns.petfinderapp.auth.service.AuthService;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@GetMapping("/login/kakao")
	public ApiResponse<LoginResponse> kakaoLogin(@RequestParam("code") String code) {
		LoginResponse data = authService.kakaoLogin(code);
		return ApiResponse.onSuccess(HttpStatus.OK, "성공", data);
	}

	@PostMapping("/logout")
	public ApiResponse<Void> kakaoLogout(@RequestHeader("Authorization") String bearer) {
		String token = bearer.replace("Bearer ", "");
		authService.logout(token);
		return ApiResponse.onSuccess(HttpStatus.NO_CONTENT, "로그아웃 성공", null);
	}
}
