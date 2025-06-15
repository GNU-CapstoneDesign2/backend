package org.duckdns.petfinderapp.domain.chat.exception;

import org.duckdns.petfinderapp.global.error.exception.ConflictException;

public class ChatRoomConflictException extends ConflictException {
	protected ChatRoomConflictException(String message) {
		super(message);
	}

	public static ChatRoomConflictException ofInvalidPostState() {
		return new ChatRoomConflictException("채팅방을 생성할 수 없는 게시글 입니다.");
	}

	public static ChatRoomConflictException ofCreatorSameAsPostAuthor() {
		return new ChatRoomConflictException("채팅방 생성자와 게시글 작성자가 동일합니다.");
	}

	public static ChatRoomConflictException ofChatRoomAlreadyExists() {
		return new ChatRoomConflictException("이미 존재하는 채팅방입니다.");
	}
}


