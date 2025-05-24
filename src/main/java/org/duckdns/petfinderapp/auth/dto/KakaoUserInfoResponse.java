package org.duckdns.petfinderapp.auth.dto;

import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.enums.UserStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponse {

	/** 카카오 회원 고유번호 */
	private String id;

	/** kakao_account 오브젝트 전체 */
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoAccount {
		/** profile 오브젝트 */
		private Profile profile;
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Profile {
		/** 사용자 닉네임 */
		private String nickname;

		/** 프로필 사진 URL (원본) */
		@JsonProperty("profile_image_url")
		private String profileImageUrl;
	}


	/**
	 * 프로필에서 꺼낸 닉네임
	 */
	public String getNickname() {
		return kakaoAccount != null && kakaoAccount.getProfile() != null
			? kakaoAccount.getProfile().getNickname()
			: null;
	}

	/**
	 * 프로필에서 꺼낸 프로필 사진 URL
	 */
	public String getProfileImageUrl() {
		return kakaoAccount != null && kakaoAccount.getProfile() != null
			? kakaoAccount.getProfile().getProfileImageUrl()
			: null;
	}

	public User toUser() {
		return User.builder()
			.provider("KAKAO")
			.providerId(id)
			.name(getNickname())
			.imageUrl(getProfileImageUrl())
			.status(UserStatus.ACTIVATE)
			.build();
	}
}
