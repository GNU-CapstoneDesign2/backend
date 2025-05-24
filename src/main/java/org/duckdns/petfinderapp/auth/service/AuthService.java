package org.duckdns.petfinderapp.auth.service;

import org.duckdns.petfinderapp.auth.dto.LoginResponse;

public interface AuthService {
	LoginResponse kakaoLogin(String code);
}
