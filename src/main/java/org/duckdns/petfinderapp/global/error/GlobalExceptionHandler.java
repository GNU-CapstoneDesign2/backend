package org.duckdns.petfinderapp.global.error;

import lombok.extern.slf4j.Slf4j;
import org.duckdns.petfinderapp.global.error.exception.BaseException;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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