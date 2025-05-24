package org.duckdns.petfinderapp.auth.service;

import java.util.List;
import java.util.Optional;

import org.duckdns.petfinderapp.auth.config.KakaoProperties;
import org.duckdns.petfinderapp.auth.dto.KakaoTokenResponse;
import org.duckdns.petfinderapp.auth.dto.KakaoUserInfoResponse;
import org.duckdns.petfinderapp.auth.dto.LoginResponse;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.repository.UserRepository;
import org.duckdns.petfinderapp.global.security.JwtTokenProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final KakaoProperties kakaoProperties;
	private final RestTemplate restTemplate;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public LoginResponse kakaoLogin(String code) {
		KakaoTokenResponse tokenResponse = requestAccessToken(code);
		KakaoUserInfoResponse userInfo = fetchUserInfo(tokenResponse.accessToken());
		User user = registerNewUserIfNeeded(userInfo);

		String accessToken = jwtTokenProvider.createAccessToken(user.getProviderId(), List.of());

		return LoginResponse.of(accessToken);
	}

	private KakaoTokenResponse requestAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoProperties.getClientId());
		params.add("client_secret", kakaoProperties.getClientSecret());
		params.add("redirect_uri", kakaoProperties.getRedirectUri());
		params.add("code", code);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
			kakaoProperties.getTokenUrl(), request, KakaoTokenResponse.class);
		return response.getBody();
	}

	private KakaoUserInfoResponse fetchUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
			kakaoProperties.getUserInfoUri(),
			HttpMethod.GET,
			request,
			KakaoUserInfoResponse.class
		);
		return response.getBody();
	}

	private User registerNewUserIfNeeded(KakaoUserInfoResponse userInfo) {
		Optional<User> existing = userRepository.findByProviderId(userInfo.getId());
		if (existing.isEmpty()) {
			User user = userInfo.toUser();
			return userRepository.save(user);
		}
		return existing.get();
	}
}
