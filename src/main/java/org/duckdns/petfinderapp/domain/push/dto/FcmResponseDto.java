package org.duckdns.petfinderapp.domain.push.dto;

import lombok.Data;

@Data
public class FcmResponseDto {
    private String token;
    private String title;
    private String message;
    private String redirect;
}
