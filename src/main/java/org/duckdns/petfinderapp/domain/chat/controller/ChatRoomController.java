package org.duckdns.petfinderapp.domain.chat.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.dto.ChatMessageDto;
import org.duckdns.petfinderapp.domain.chat.dto.ChatRoomDto;
import org.duckdns.petfinderapp.domain.chat.dto.ChatRoomPostDto;
import org.duckdns.petfinderapp.domain.chat.dto.request.ChatRoomCreateResponse;
import org.duckdns.petfinderapp.domain.chat.dto.response.ChatRoomCreateRequest;
import org.duckdns.petfinderapp.domain.chat.dto.response.UnreadMessageResponse;
import org.duckdns.petfinderapp.domain.chat.service.ChatMessageService;
import org.duckdns.petfinderapp.domain.chat.service.ChatRoomService;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.global.template.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
public class ChatRoomController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/{roomId}/messages")
    public ApiResponse<Page<ChatMessageDto>> getChatRoomMessages(
            @AuthenticationPrincipal @NotNull User user,
            @PathVariable @NotNull Long roomId,
            Pageable pageable) {

        Page<ChatMessageDto> data = chatMessageService.getChatRoomMessages(user, roomId, pageable);
        return ApiResponse.onSuccess(HttpStatus.OK, "채팅 메시지 조회 성공", data);
    }

    // 채팅방 목록 조회
    @GetMapping("")
    public ApiResponse<List<ChatRoomDto>> getChatRoomList(
            @AuthenticationPrincipal @NotNull User user
    ) {
        List<ChatRoomDto> chatRooms = chatRoomService.getChatRoomList(user.getId());
        return ApiResponse.onSuccess(HttpStatus.OK, "채팅방 목록 조회 성공", chatRooms);
    }

    // 상세조회
    @GetMapping("/{roomId}")
    public ApiResponse<ChatRoomPostDto> getChatRoomPost(
            @PathVariable @NotNull Long roomId,
            @AuthenticationPrincipal @NotNull User user
    ){
        ChatRoomPostDto chatRoomPost = chatRoomService.getChatRoomPost(roomId, user.getId());

        return ApiResponse.onSuccess(HttpStatus.OK, "채팅방 상세 조회 성공", chatRoomPost);
    }

    @PostMapping("")
    public ApiResponse<ChatRoomCreateResponse> createChatRoom(
            @AuthenticationPrincipal User user,
            @RequestBody ChatRoomCreateRequest chatRoomCreateRequest
    ) {

        ChatRoomCreateResponse chatRoom = chatRoomService.createChatRoom(user, chatRoomCreateRequest);
        return ApiResponse.onSuccess(HttpStatus.CREATED, "채팅방 생성 성공", chatRoom);
    }

    @GetMapping("/unread")
    public ApiResponse<UnreadMessageResponse> getUnreadChatRooms(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(
            HttpStatus.OK,
            "전체 채팅 안 읽은 수 조회 성공",
            chatRoomService.getUnreadChatRoomMessageCount(user));
    }
}
