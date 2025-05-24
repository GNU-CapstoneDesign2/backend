package org.duckdns.petfinderapp.domain.user.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserUpdateRequest (
	@Length(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
	String name,
	@NotBlank(message = "이미지 URL은 필수입니다.")
	String imageUrl
) {
}
