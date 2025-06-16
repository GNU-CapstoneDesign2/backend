package org.duckdns.petfinderapp.domain.post.enums;

public enum PostState {
	ADOPT,  // 입양 (보호중)
	LOST,   // 실종
	SIGHT,  // 목격
	NOTICE, // 공고
	END     // 보호종료
	;

	public String toKoreanString() {
		return switch (this) {
			case NOTICE -> "공고";
			case ADOPT -> "입양";
			case LOST -> "실종";
			case SIGHT -> "목격";
			case END -> "보호종료";
		};
	}

	public static PostState fromString(String state) {
		if (state == null) {
			return null;
		}
		return switch (state.toLowerCase()) {
			case "notice" -> NOTICE;
			case "protect" -> ADOPT;
			case "finish" -> END;
			default -> null;
		};
	}
}
