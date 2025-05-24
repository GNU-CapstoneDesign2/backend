package org.duckdns.petfinderapp.domain.user.service;

import org.duckdns.petfinderapp.domain.user.dto.request.UserUpdateRequest;
import org.duckdns.petfinderapp.domain.user.dto.response.UserInfoResponse;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public UserInfoResponse updateUserInfo(User user, UserUpdateRequest userUpdateRequest) {

		user.updateUserInfo(userUpdateRequest.name(), userUpdateRequest.imageUrl());
		User savedUser = userRepository.save(user);

		return UserInfoResponse.of(savedUser);
	}

	@Override
	public void deleteUser(User user) {
		user.deactivate();
		userRepository.save(user);
	}
}
