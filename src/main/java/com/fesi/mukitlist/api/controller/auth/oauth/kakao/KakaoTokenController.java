package com.fesi.mukitlist.api.controller.auth.oauth.kakao;

import static com.fesi.mukitlist.core.auth.GrantType.*;
import static com.fesi.mukitlist.core.auth.TokenType.*;
import static com.fesi.mukitlist.core.auth.application.constant.UserType.*;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fesi.mukitlist.api.controller.auth.oauth.kakao.client.KakaoTokenClient;
import com.fesi.mukitlist.api.controller.auth.oauth.kakao.request.KakaoServiceCreateRequest;
import com.fesi.mukitlist.api.controller.auth.oauth.kakao.request.KakaoUserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.oauth.kakao.response.KakaoTokenResponse;
import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.repository.TokenRepository;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.core.auth.Token;
import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.auth.oauth.KakaoUserInfo;
import com.fesi.mukitlist.core.auth.oauth.OAuth2UserInfo;
import com.fesi.mukitlist.domain.service.auth.AuthenticationService;
import com.fesi.mukitlist.domain.service.auth.JwtService;
import com.fesi.mukitlist.domain.service.auth.application.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class KakaoTokenController {
	private final KakaoTokenClient kakaoTokenClient;
	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenRepository tokenRepository;

	@Value("${kakao.client.id}")
	private String clientId;

	@Value("${kakao.client.secret}")
	private String clientSecret;

	@GetMapping("/kakao/login")
	public String redirectToKakaoLogin() {
		String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId +
			"&redirect_uri=" + "http://localhost:8080/api/auth/kakao/callback" + "&response_type=code";
		return "redirect:" + kakaoAuthUrl;
	}

	@GetMapping("/kakao/callback")
	public ResponseEntity<AuthenticationResponse> loginCallback(@RequestParam String code,
		HttpServletResponse response) {
		String contentType = "application/x-www-form-urlencoded;charset=UTF-8";

		KakaoTokenResponse kakaoTokenResponse = kakaoTokenClient.requestKakaoToken(
			"authorization_code",
			clientId,
			clientSecret,
			code,
			"http://localhost:8080/api/auth/kakao/callback"
		);

		log.info("Kakao Token Response: " + kakaoTokenResponse);

		OAuth2UserInfo oAuth2UserInfo = getKakaoUserInfo(kakaoTokenResponse.access_token());
		String email = oAuth2UserInfo.getEmail();

		User user = userRepository.findByEmail(email).orElseGet(() -> {
			String providerId = oAuth2UserInfo.getProviderId();
			String nickname =
				oAuth2UserInfo.getNickname() != null ? oAuth2UserInfo.getNickname() : oAuth2UserInfo.getName();
			String password = passwordEncoder.encode("temporaryPassword");

			KakaoServiceCreateRequest userCreateRequest = KakaoUserCreateRequest.toServiceRequest(
				email,
				nickname,
				KAKAO,
				providerId);
			return userService.createKaKaoUser(userCreateRequest);
		});
		PrincipalDetails kakaoAccount = new PrincipalDetails(user,
			Map.of("kakao_account", oAuth2UserInfo.getProviderId()));
		String accessToken = jwtService.generateAccessToken(kakaoAccount);
		String refreshToken = jwtService.generateRefreshToken(kakaoAccount);
		tokenRepository.save(Token.of(refreshToken, BEARER, REFRESH, false, user));
		authenticationService.addRefreshTokenToCookie(refreshToken);
		// 루트도메인
		return ResponseEntity.ok(AuthenticationResponse.of(accessToken));
	}

	private OAuth2UserInfo getKakaoUserInfo(String accessToken) {
		String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> response = restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.GET, entity, Map.class);

		Map<String, Map<String, Object>> kakaoAccount = (Map<String, Map<String, Object>>)Objects.requireNonNull(
			response.getBody()).get("kakao_account");
		String providerId = String.valueOf(response.getBody().get("id"));

		return new KakaoUserInfo(kakaoAccount, providerId);
	}

}
