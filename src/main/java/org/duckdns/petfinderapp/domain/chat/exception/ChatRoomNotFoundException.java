package org.duckdns.petfinderapp.domain.chat.exception;

import org.duckdns.petfinderapp.global.error.exception.NotFoundGroupException;

public class ChatRoomNotFoundException extends NotFoundGroupException {
    protected ChatRoomNotFoundException(String message) {
        super(message);
    }

    public static ChatRoomNotFoundException missingChatRoom() {
        return new ChatRoomNotFoundException("해당 채팅방이 존재하지 않습니다.");
    }
}
