package org.duckdns.petfinderapp.domain.user.exception;

import org.duckdns.petfinderapp.global.error.exception.NotFoundGroupException;

public class UserNotFoundException extends NotFoundGroupException {

	protected UserNotFoundException(String message) {
		super(message);
	}

	public static UserNotFoundException missingUser() {
		return new UserNotFoundException("해당 유저 정보가 존재하지 않습니다.");
	}
}
