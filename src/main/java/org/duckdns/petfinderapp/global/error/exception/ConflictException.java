package org.duckdns.petfinderapp.global.error.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

	protected ConflictException(String message) {
		super(HttpStatus.CONFLICT, message);
	}
}
