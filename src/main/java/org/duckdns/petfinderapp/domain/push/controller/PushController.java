package org.duckdns.petfinderapp.domain.push.controller;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.push.dto.PushDto;
import org.duckdns.petfinderapp.domain.push.entity.Push;
import org.duckdns.petfinderapp.domain.push.service.PushService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PushController {
    private final PushService pushService;

    @GetMapping("/notices")
    public ResponseEntity<ApiResponse<List<PushDto>>> getPushs(@AuthenticationPrincipal User user) {
        List<PushDto> pushs = pushService.getPushNotifications(user);
        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "알림 목록 조회 성공", pushs));
    }

    @PatchMapping("notice/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        pushService.isRead(user, id);
        return ResponseEntity.ok(ApiResponse.onSuccess(HttpStatus.OK, "알림 읽음 처리 성공", null));
    }
}
