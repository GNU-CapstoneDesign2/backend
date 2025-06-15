package org.duckdns.petfinderapp.domain.push.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.push.dto.PushDto;
import org.duckdns.petfinderapp.domain.push.entity.Push;
import org.duckdns.petfinderapp.domain.push.repository.PushRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushService {
    private final PushRepository pushRepository;

    public List<PushDto> getPushNotifications(User user) {
        List<Push> pushList = pushRepository.findByUserOrderByCreatedAtDesc(user);
        return pushList.stream()
                .map(PushDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void isRead(User user, Long pushId) {
        Push push = pushRepository.findById(pushId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        // 해당 사용자의 알림인지 확인
        if (!push.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        push.pushread();
    }
}
