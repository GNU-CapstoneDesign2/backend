package org.duckdns.petfinderapp.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.duckdns.petfinderapp.domain.user.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Long userId;
    private String name;
    private String profileImage;
    private boolean isMe;

    public static ParticipantDto from(User user, Long currentUserId) {
        return ParticipantDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .profileImage(user.getImageUrl())
                .isMe(user.getId().equals(currentUserId))
                .build();
    }

}
