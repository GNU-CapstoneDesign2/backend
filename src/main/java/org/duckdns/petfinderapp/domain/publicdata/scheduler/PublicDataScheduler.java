package org.duckdns.petfinderapp.domain.publicdata.scheduler;

import org.duckdns.petfinderapp.domain.publicdata.service.AdoptSyncService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicDataScheduler {
	private final AdoptSyncService adoptSyncService;

	// 5분마다 구조 동물 데이터를 추가하는 스케줄러
	@Scheduled(fixedRate = 5 * 60 * 1000) // 매 5분마다
	public void addAdopts() {
		log.info("구조 동물을 추가합니다.");
		adoptSyncService.addNewAdopts()
			.doOnError(e -> log.error("구조 동물 데이터 추가 중 에러 발생:", e))
			.subscribe();
	}

	// 매일 오전 2시에 전날 수정된 구조 동물 데이터를 갱신하는 스케줄러
	@Scheduled(cron = "0 0 2 * * ?") // 매일 오전 2시
	public void updateAllAdopts() {
		log.info("전체 구조 동물 데이터를 갱신합니다.");
		adoptSyncService.refreshAllAdopts()
			.doOnError(e -> log.error("전체 구조 동물 데이터 갱신 중 에러 발생:", e))
			.subscribe();
	}
}
