package org.duckdns.petfinderapp.domain.chat.dto.request;

import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;

import lombok.Builder;

@Builder
public record ChatRoomCreateResponse(
	Long roomId
) {
	public static ChatRoomCreateResponse of(ChatRoom chatRoom) {
		return ChatRoomCreateResponse.builder()
			.roomId(chatRoom.getId())
			.build();
	}
}
