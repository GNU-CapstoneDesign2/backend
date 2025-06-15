package org.duckdns.petfinderapp.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.post.dto.response.PostSummaryResponse;
import org.duckdns.petfinderapp.domain.post.dto.response.ResCommon;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomPostDto {
    private Long roomId;
    private PostSummaryResponse post;
    private List<ParticipantDto> participants;

    public static ChatRoomPostDto from(ChatRoom chatRoom, PostSummaryResponse postSummary, Long currentUserId) {

        // 참여자 리스트
        List<ParticipantDto> participants = Arrays.asList(
                ParticipantDto.from(chatRoom.getSender(), currentUserId),
                ParticipantDto.from(chatRoom.getReceiver(), currentUserId)
        );

        return ChatRoomPostDto.builder()
                .roomId(chatRoom.getId())
                .post(postSummary)
                .participants(participants)
                .build();
    }
}
