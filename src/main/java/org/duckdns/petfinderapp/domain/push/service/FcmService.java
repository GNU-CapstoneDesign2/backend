package org.duckdns.petfinderapp.domain.push.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.push.entity.FcmToken;
import org.duckdns.petfinderapp.domain.push.repository.FcmTokenRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void saveToken(User user, String token) {
        FcmToken tokenEntity = fcmTokenRepository.findByUserId(user)
                .map(t -> {
                    t.updateToken(token);
                    return t;
                })
                .orElse(new FcmToken(user, token));

        fcmTokenRepository.save(tokenEntity);
    }

    public void sendMessage(User user, String title, String message) {
        String token = fcmTokenRepository.findByUserId(user)
                .map(FcmToken::getToken)
                .orElseThrow(() -> new IllegalArgumentException("사용자 토큰을 찾을 수 없습니다."));

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();

        Message fcmMessage = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            System.out.println("푸시 알림 전송 성공: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("푸시 알림 전송 실패", e);
        }
    }
}
