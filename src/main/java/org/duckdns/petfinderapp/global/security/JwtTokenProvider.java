package org.duckdns.petfinderapp.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expire-length-ms}")
	private long validityInMilliseconds;

	// Build a secure Key from your secret
	private SecretKey getSigningKey() {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		// Keys.hmacShaKeyFor returns a SecretKey
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// 1) 토큰 생성
	public String createToken(String userId, List<String> roles) {
		Map<String,Object> claims = Map.of("roles", roles);

		Date now = new Date();
		Date exp = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()
			.claims(claims)
			.subject(userId)
			.issuedAt(now)
			.expiration(exp)
			.signWith(getSigningKey())
			.compact();
	}

	public String createAccessToken(String  userId, List<String> roles) {
		return createToken(userId, roles);
	}

	// 2) 토큰에서 사용자 ID 추출
	public String getUserId(String token) {
		Claims body = Jwts.parser()
			.verifyWith(getSigningKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();

		return body.getSubject();
	}

	// 3) 유효성 검사
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
