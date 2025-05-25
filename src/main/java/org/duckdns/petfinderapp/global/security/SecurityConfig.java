package org.duckdns.petfinderapp.global.security;

import java.util.List;

import org.duckdns.petfinderapp.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtTokenProvider jwtTokenProvider;
	private final RestAuthenticationEntryPoint entryPoint;
	private final RestAccessDeniedHandler accessDeniedHandler;
	private final UserRepository userRepository;
	private final TokenBlackListService blackListService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			// CSRF 완전 비활성화
			.csrf(AbstractHttpConfigurer::disable)
			// stateless 세션 정책
			.sessionManagement(sm -> sm
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(entryPoint)
				.accessDeniedHandler(accessDeniedHandler)
			)
			// 권한 검사 설정
			.authorizeHttpRequests(auth -> auth
				// 카카오 로그인 엔드포인트는 인증 없이 허용
				.requestMatchers(
					"/auth/login/kakao",
					"/ws/**"
				).permitAll()
				// 그 외의 모든 요청은 인증 필요
				.anyRequest().authenticated()
			)
		;

			// JWT 필터 적용
		http.addFilterBefore(
			new JwtAuthenticationFilter(jwtTokenProvider, userRepository, blackListService),
			UsernamePasswordAuthenticationFilter.class
		);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(List.of("*"));
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS", "WEBSOCKET"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
