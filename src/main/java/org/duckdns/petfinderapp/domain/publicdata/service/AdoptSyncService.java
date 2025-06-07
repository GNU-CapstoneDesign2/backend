package org.duckdns.petfinderapp.domain.publicdata.service;

import reactor.core.publisher.Mono;

public interface AdoptSyncService {
	/** 최근 공고만 추가 저장 (incremental sync) */
	Mono<Void> addNewAdopts();

	/** 전체 데이터 갱신 (full sync) */
	Mono<Void> refreshAllAdopts();
}
