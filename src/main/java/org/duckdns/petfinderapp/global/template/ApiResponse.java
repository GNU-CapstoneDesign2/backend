package org.duckdns.petfinderapp.global.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"code", "message", "result"})
public record ApiResponse<T>(int code, String message, @JsonInclude(JsonInclude.Include.NON_NULL) T result) {
    // 성공 응답 생성 메서드
    public static <T> ApiResponse<T> onSuccess(HttpStatus status, String message, T result) {
        return new ApiResponse<>(status.value(), message, result);
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> onFailure(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), message, null);
    }
}