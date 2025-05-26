package org.duckdns.petfinderapp.global.error.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedGroupException extends BaseException{
    protected AccessDeniedGroupException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
