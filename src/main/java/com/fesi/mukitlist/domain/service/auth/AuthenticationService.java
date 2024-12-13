package com.fesi.mukitlist.domain.service.auth;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import java.io.IOException;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.repository.TokenRepository;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.core.auth.GrantType;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.core.auth.Token;
import com.fesi.mukitlist.core.auth.TokenType;
import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.domain.service.auth.request.AuthenticationServiceRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService {
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public void saveUserToken(PrincipalDetails principalDetails, String refreshToken) {
		Token existingToken = tokenRepository.findByUserAndToken(principalDetails.getUser().getId(), refreshToken);
		if (existingToken != null) {
			existingToken = Token.builder()
				.user(principalDetails.getUser())
				.token(refreshToken)
				.grantType(GrantType.BEARER)
				.tokenType(TokenType.REFRESH)
				.expired(false)
				.build();
			tokenRepository.save(existingToken);
		} else {
			Token token = Token.builder()
				.user(principalDetails.getUser())
				.token(refreshToken)
				.tokenType(TokenType.REFRESH)
				.expired(false)
				.build();
			tokenRepository.save(token);
		}
	}

	public AuthenticationResponse authenticate(AuthenticationServiceRequest request) {
		Authentication authenticate = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.email(), request.password()));

		User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new AppException(NOT_FOUND_USER));
		PrincipalDetails principalDetails = new PrincipalDetails(user);

		String accessToken = jwtService.generateAccessToken(principalDetails);
		String refreshToken = tokenRepository.findByUser(principalDetails.getUser())
			.map(Token::getToken)
			.orElseGet(() -> {
				String newToken = jwtService.generateRefreshToken(principalDetails);
				Token token = Token.builder()
					.user(principalDetails.getUser())
					.token(newToken)
					.tokenType(TokenType.REFRESH)
					.expired(false)
					.build();
				tokenRepository.save(token);
				return newToken;
			});

		addRefreshTokenToCookie(refreshToken);
		return AuthenticationResponse.builder()
			.accessToken(accessToken)
			.build();
	}

	public AuthenticationResponse refreshToken(String token) throws IOException {
		jwtService.isRefreshTokenValid(token);
		Token tokenInfo = tokenRepository.findByToken(token);
		String accessToken = "";
		if (tokenRepository.existsTokenByUserIdAndToken(tokenInfo.getUser().getId(), token)) {
			accessToken = jwtService.generateAccessToken(new PrincipalDetails(tokenInfo.getUser()));
		}
		return AuthenticationResponse.of(accessToken);
	}

	public void addRefreshTokenToCookie(String refreshToken) {
		ResponseCookie.from("refresh-token", refreshToken)
			.httpOnly(true)
			.sameSite("None")
			.secure(true)
			.path("/")
			.maxAge(60 * 60 * 24 * 7)
			.build();
	}

}