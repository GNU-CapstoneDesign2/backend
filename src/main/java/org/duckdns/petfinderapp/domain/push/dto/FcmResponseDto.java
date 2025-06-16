package org.duckdns.petfinderapp.domain.push.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FcmResponseDto {
    private String token;
    private String title;
    private String message;
    private String redirect;

    public static FcmResponseDto of(String token, String title, String message, String redirect) {
        return FcmResponseDto.builder()
            .token(token)
            .title(title)
            .message(message)
            .redirect(redirect)
            .build();
    }
}
