package org.duckdns.petfinderapp.global.security;

import java.io.IOException;

import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			if (jwtTokenProvider.validateToken(token)) {
				// 1) 토큰에서 사용자 ID 획득
				String userId = jwtTokenProvider.getUserId(token);

				// 2) UserDetailsService 로부터 실제 사용자 정보 조회
				User user = userRepository.findByProviderId(userId)
					.orElseThrow();

				// 4) principal 에 엔티티를, credentials 는 null, 권한 목록 세팅
				UsernamePasswordAuthenticationToken auth =
					new UsernamePasswordAuthenticationToken(user, null, null);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		filterChain.doFilter(request, response);
	}
}
