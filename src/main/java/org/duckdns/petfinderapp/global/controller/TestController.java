package org.duckdns.petfinderapp.global.controller;

import lombok.RequiredArgsConstructor;

import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.global.error.exception.NotFoundException;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/success")
    public ApiResponse<String> testSuccess() {
        return ApiResponse.onSuccess(
                HttpStatus.OK,
                "정상적으로 호출되었습니다.",
                "success");
    }

    @GetMapping("/error")
    public ApiResponse<String> testFailure() {
        throw new TestException();
    }

    @GetMapping("/unhandled")
    public ApiResponse<String> testUnhandled() {
        throw new RuntimeException("예상치 못한 오류");
    }

    private static class TestException extends NotFoundException {
        TestException() {
            super("hello");
        }
    }

    @GetMapping("/user")
    public ApiResponse<User> testUser(@AuthenticationPrincipal User user) {
        return ApiResponse.onSuccess(
                HttpStatus.OK,
                "정상적으로 호출되었습니다.",
                user);
    }
}
