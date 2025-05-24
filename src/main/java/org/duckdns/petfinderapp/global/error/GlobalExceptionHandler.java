package org.duckdns.petfinderapp.global.error;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.duckdns.petfinderapp.global.error.exception.BaseException;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
        log.error("Error [{}]: {}", ex.getStatus().value(), ex.getMessage(), ex);
        ApiResponse<Void> body = ApiResponse.onFailure(
                ex.getStatus(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(ex.getStatus())
                .body(body);
    }

    // 1) DTO 검증 실패 잡기
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.warn("Validation failed: {}", errors);

        ApiResponse<Map<String, String>> body = ApiResponse.onFailure(
            HttpStatus.BAD_REQUEST,
            "입력 값이 유효하지 않습니다.",
            errors    // ApiResponse에 data(payload) 필드를 지원해야 합니다.
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body);
    }

    // 2) 그 외 모든 예외는 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOtherException(Exception ex) {
        log.error("[UnhandledException] ", ex);
        ApiResponse<Void> body = ApiResponse.onFailure(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 오류가 발생했습니다."
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
