package org.duckdns.petfinderapp.domain.push.dto;

import lombok.Data;

@Data
public class FcmResponseDto {
    private Long userId;
    private String token;
    private String title;
    private String message;
}
