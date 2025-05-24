package org.duckdns.petfinderapp.domain.user.service;

import org.duckdns.petfinderapp.domain.user.dto.request.UserUpdateRequest;
import org.duckdns.petfinderapp.domain.user.dto.response.UserInfoResponse;
import org.duckdns.petfinderapp.domain.user.entity.User;

public interface UserService {
	UserInfoResponse updateUserInfo(User user, UserUpdateRequest userUpdateRequest);
}
