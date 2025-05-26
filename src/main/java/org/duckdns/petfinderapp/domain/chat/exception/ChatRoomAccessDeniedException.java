package org.duckdns.petfinderapp.domain.chat.exception;

import org.duckdns.petfinderapp.global.error.exception.AccessDeniedGroupException;

public class ChatRoomAccessDeniedException extends AccessDeniedGroupException {
    protected ChatRoomAccessDeniedException(String message) {
        super(message);
    }

    public static ChatRoomAccessDeniedException accessDenied() {
        return new ChatRoomAccessDeniedException("해당 채팅방에 접근할 수 없습니다.");
    }
}
