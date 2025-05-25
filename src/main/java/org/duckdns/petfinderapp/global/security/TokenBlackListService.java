package org.duckdns.petfinderapp.global.security;

import java.time.Duration;
import java.time.Instant;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {
	private final Cache blacklist;

	public TokenBlackListService(CacheManager cacheManager) {
		this.blacklist = cacheManager.getCache("blackList");
	}

	public void addToBlackList(String jti, Duration ttl) {
		blacklist.put(jti, Instant.now().plus(ttl));
	}

	public boolean isBlackListed(String jti) {
		Instant expiresAt = blacklist.get(jti, Instant.class);
		if (expiresAt == null) {
			return false;
		}
		if (Instant.now().isAfter(expiresAt)) {
			blacklist.evict(jti); // Remove expired token
			return false;
		}
		return true;
	}
}
