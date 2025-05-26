package org.duckdns.petfinderapp.domain.push.controller;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.push.dto.FcmResponseDto;
import org.duckdns.petfinderapp.domain.push.service.FcmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("notice/token")
    public ResponseEntity<Void> saveToken(@RequestBody FcmResponseDto dto){
        fcmService.saveToken(dto.getUserId(), dto.getToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("notice/send")
    public ResponseEntity<Void> send(@RequestBody FcmResponseDto dto){
        fcmService.sendMessage(dto.getUserId(), dto.getTitle(), dto.getMessage());
        return ResponseEntity.ok().build();
    }
}
