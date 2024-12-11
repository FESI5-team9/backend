package com.fesi.mukitlist.api.service.auth;

import java.io.IOException;
import java.util.Optional;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.exception.ExceptionCode;
import com.fesi.mukitlist.api.service.oauth.KakaoLoginService;
import com.fesi.mukitlist.api.service.oauth.request.KakaoSocialLoginRequest;
import com.fesi.mukitlist.api.service.oauth.request.KakaoUserCreateRequest;
import com.fesi.mukitlist.api.service.oauth.response.LoginResponse;
import com.fesi.mukitlist.api.service.oauth.response.SocialUserResponse;
import com.fesi.mukitlist.domain.auth.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fesi.mukitlist.api.controller.auth.request.UserUpdateRequest;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.api.service.auth.request.UserServiceCreateRequest;
import com.fesi.mukitlist.api.service.auth.response.UserInfoResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.global.aws.S3Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final KakaoLoginService kakaoLoginService;
	private final JwtService jwtService;
	private final AuthenticationService authenticationService;
	private final S3Service s3Service;

	public User createUser(UserServiceCreateRequest request) {
		String encodePassword = passwordEncoder.encode(request.password()); // 해싱하는 부분
		User user = User.of(request, encodePassword);
		return userRepository.save(user);
	}

	public UserInfoResponse getUserInfo(User user) {
		return UserInfoResponse.of(user);
	}

	public UserInfoResponse updateUser(User user, UserUpdateRequest request) throws IOException {

		Optional.ofNullable(request.nickname()).ifPresent(user::updateNickname);
		if (request.image() != null) {
			String storedName = s3Service.upload(request.image(), request.image().getOriginalFilename());
			user.updateImage(storedName);
		}

		return UserInfoResponse.of(user);
	}

	public LoginResponse doSocialLogin(String authorizationCode) throws IOException {

		String accessToken = kakaoLoginService.getAccessToken(authorizationCode);

		SocialUserResponse kakaoUserResponse = kakaoLoginService.getUserInfo(accessToken);
		validateSocialUserResponse(kakaoUserResponse);

		Optional<User> existingUser = userRepository.findByEmail(kakaoUserResponse.email());
		User user;
		if (existingUser.isPresent()) {
			user = existingUser.get();
		} else {
			KakaoUserCreateRequest userCreateRequest = new KakaoUserCreateRequest(
					kakaoUserResponse.email(),
					kakaoUserResponse.nickname(),
					"default_password", // Temporary password
					kakaoLoginService.getServiceName()
			);
			user = User.of(userCreateRequest);
			user = userRepository.save(user);
		}

		String jwtAccessToken = generateJwtToken(user);
		String jwtRefreshToken = generateJwtRefreshToken(user);

		authenticationService.saveUserToken(new PrincipalDetails(user), jwtRefreshToken);

		return new LoginResponse(ExceptionCode.FORBIDDEN.getCode(), jwtAccessToken);
	}



	private String generateJwtToken(User user) {
		PrincipalDetails principalDetails = new PrincipalDetails(user);
		return jwtService.generateToken(principalDetails);
	}

	private String generateJwtRefreshToken(User user) {
		PrincipalDetails principalDetails = new PrincipalDetails(user);
		return jwtService.generateRefreshToken(principalDetails);
	}

	private void validateSocialUserResponse(SocialUserResponse socialUserResponse) {
		if (socialUserResponse == null) {
			throw new AppException(ExceptionCode.NOT_FOUND_USER);
		}

		if (socialUserResponse.email() == null || socialUserResponse.email().isEmpty()) {
			throw new AppException(ExceptionCode.NOT_FOUND_USER);
		}

		if (socialUserResponse.nickname() == null || socialUserResponse.nickname().isEmpty()) {
			throw new AppException(ExceptionCode.NOT_FOUND_USER);
		}
	}
}
