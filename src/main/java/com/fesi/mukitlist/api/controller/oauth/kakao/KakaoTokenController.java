package com.fesi.mukitlist.api.controller.oauth.kakao;

import com.fesi.mukitlist.api.controller.oauth.kakao.client.KakaoTokenClient;
import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.oauth.kakao.response.KakaoTokenResponse;
import com.fesi.mukitlist.api.service.auth.UserService;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.oauth.OAuth2UserInfo;
import com.fesi.mukitlist.domain.oauth.KakaoUserInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Map;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class KakaoTokenController {

    private final KakaoTokenClient kakaoTokenClient;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public String loginCallback(@RequestParam String code, HttpServletResponse response) {
        String contentType = "application/x-www-form-urlencoded;charset=UTF-8";

        KakaoTokenResponse kakaoTokenResponse = kakaoTokenClient.requestKakaoToken(
                "authorization_code",
                clientId,
                clientSecret,
                code,
                "http://localhost:8080/api/auth/kakao/callback"
        );

        log.info("Kakao Token Response: " + kakaoTokenResponse);

        String accessToken = kakaoTokenResponse.access_token();
        String refreshToken = kakaoTokenResponse.refresh_token();

        OAuth2UserInfo oAuth2UserInfo = getKakaoUserInfo(accessToken);

        String email = oAuth2UserInfo.getEmail();
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            String provider = "kakao";
            String providerId = oAuth2UserInfo.getProviderId();
            String username = provider + "_" + providerId;
            String nickname = oAuth2UserInfo.getNickname() != null ? oAuth2UserInfo.getNickname() : oAuth2UserInfo.getName();
            String password = passwordEncoder.encode("temporaryPassword");

            UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            return userService.createUser(userCreateRequest.toServiceRequest());
        });

        Cookie refreshTokenCookie = new Cookie("Kakao_Refresh_Token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(refreshTokenCookie);

        return "redirect:/?access_token=" + accessToken;
    }

    private OAuth2UserInfo getKakaoUserInfo(String accessToken) {
        String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.GET, entity, Map.class);

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        String providerId = String.valueOf(response.getBody().get("id"));

        return new KakaoUserInfo(kakaoAccount, providerId);
    }

}
