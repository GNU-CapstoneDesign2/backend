package org.duckdns.petfinderapp.global.error.exception;

import org.springframework.http.HttpStatus;

public class NotFoundGroupException extends BaseException {
	protected NotFoundGroupException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
