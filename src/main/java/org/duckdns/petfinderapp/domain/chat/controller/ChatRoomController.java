package org.duckdns.petfinderapp.domain.chat.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.service.ChatMessageService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
public class ChatRoomController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{roomId}/messages")
    public ApiResponse<Page<ChatMessageDto>> getChatRoomMessages(
            @AuthenticationPrincipal @NotNull User user,
            @PathVariable @NotNull Long roomId,
            Pageable pageable) {

        Page<ChatMessageDto> data = chatMessageService.getChatRoomMessages(user, roomId, pageable);
        return ApiResponse.onSuccess(HttpStatus.OK, "채팅 메시지 조회 성공", data);
    }
}
