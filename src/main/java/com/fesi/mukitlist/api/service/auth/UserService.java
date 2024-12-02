package com.fesi.mukitlist.api.service.auth;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fesi.mukitlist.api.controller.auth.request.UserUpdateRequest;
import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.exception.ExceptionCode;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.api.service.auth.request.UserServiceCreateRequest;
import com.fesi.mukitlist.api.service.auth.response.UserInfoResponse;
import com.fesi.mukitlist.domain.auth.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User createUser(UserServiceCreateRequest request) {
		String encodePassword = passwordEncoder.encode(request.password()); // 해싱하는 부분
		User user = User.of(request, encodePassword);
		return userRepository.save(user);
	}

	public UserInfoResponse getUserInfo(Long userId) {
		return UserInfoResponse.of(
			userRepository.findById(userId).orElseThrow(() -> new AppException(ExceptionCode.NOT_FOUND_USER)));
	}

    public UserInfoResponse updateUser(Long id, UserUpdateRequest request, MultipartFile image) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ExceptionCode.NOT_FOUND_USER));

        Optional.ofNullable(request.nickname()).ifPresent(user::updateNickname);

        return UserInfoResponse.of(user);
    }
}