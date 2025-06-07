package org.duckdns.petfinderapp.domain.post.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PetType {
	DOG("개"),
	CAT("고양이"),
	ETC("기타");

	// JSON으로부터 오는 한글(또는 기타 문자열) 값을 담을 필드
	private final String korean;

	PetType(String korean) {
		this.korean = korean;
	}

	/**
	 * 서버(JSON)로 응답된 문자열(예: "개" 또는 "고양이" 등)을 이 메서드를 통해
	 * 해당 enum 상수로 변환해 줍니다.
	 */
	@JsonCreator
	public static PetType from(String value) {
		if (value == null) {
			return ETC;   // null처리 시 기본값
		}
		return switch (value.trim()) {
			case "개" -> DOG;
			case "고양이" -> CAT;
			case "기타" ->   // 혹은 JSON에서 기타 동물은 "기타" 등으로 올 수 있다면
				ETC;
			default ->
				// 만약 JSON에 예상치 못한 문자열이 들어오면
				// 기본적으로 ETC로 처리하거나, IllegalArgumentException 던질 수도 있음
				ETC;
		};
	}

	@JsonValue
	public String toJson() {
		return this.korean;
	}
}
