package org.duckdns.petfinderapp.domain.push.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.duckdns.petfinderapp.domain.push.entity.Push;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushDto {
    private Long id;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public static PushDto from(Push push) {
        return PushDto.builder()
                .id(push.getId())
                .title(push.getTitle())
                .message(push.getMessage())
                .isRead(push.getIsRead())
                .createdAt(push.getCreatedAt())
                .build();
    }
}

