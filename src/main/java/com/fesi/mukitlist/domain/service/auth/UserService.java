package com.fesi.mukitlist.domain.service.auth;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fesi.mukitlist.api.controller.auth.request.UserUpdateRequest;
import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.exception.ExceptionCode;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.domain.service.auth.request.UserServiceCreateRequest;
import com.fesi.mukitlist.domain.service.auth.response.UserInfoResponse;
import com.fesi.mukitlist.core.auth.User;
import com.fesi.mukitlist.domain.service.aws.S3Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final S3Service s3Service;

	public User createUser(UserServiceCreateRequest request) {
		String encodePassword = passwordEncoder.encode(request.password()); // 해싱하는 부분
		User user = User.of(request, encodePassword);
		return userRepository.save(user);
	}

	public UserInfoResponse getUserInfo(User user) {
		return UserInfoResponse.of(userRepository.findById(user.getId()).orElseThrow(() -> new AppException(
			ExceptionCode.NOT_FOUND_USER)));
	}

	public UserInfoResponse updateUser(User user, UserUpdateRequest request) throws IOException {

		Optional.ofNullable(request.nickname()).ifPresent(user::updateNickname);
		if (request.image() != null) {
			String storedName = s3Service.upload(request.image(), request.image().getOriginalFilename());
			user.updateImage(storedName);
		}

		return UserInfoResponse.of(user);
	}

	public boolean checkEmail(String email) {
		return userRepository.existsUserByEmail(email);
	}

	public boolean checkNickname(String nickname) {
		return userRepository.existsUserByNickname(nickname);
	}
}