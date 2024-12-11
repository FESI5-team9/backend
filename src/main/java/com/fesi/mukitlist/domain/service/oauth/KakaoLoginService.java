package com.fesi.mukitlist.api.service.oauth;

import com.fesi.mukitlist.api.controller.auth.oauth.KakaoAuthApi;
import com.fesi.mukitlist.api.controller.auth.oauth.KakaoUserApi;
import com.fesi.mukitlist.api.controller.auth.oauth.util.GsonLocalDateTimeAdapter;
import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.exception.ExceptionCode;
import com.fesi.mukitlist.api.service.oauth.response.KaKaoLoginResponse;
import com.fesi.mukitlist.api.service.oauth.response.SocialAuthResponse;

import com.fesi.mukitlist.api.service.oauth.response.SocialUserResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.auth.constant.UserType;
import com.nimbusds.jose.shaded.gson.Gson;

import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService  {

    private final KakaoAuthApi kakaoAuthApi;
    private final KakaoUserApi kakaoUserApi;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.client.redirect_url}")
    private String redirectUrl;


    public String getAccessToken(String authorizationCode) {
        ResponseEntity<?> response = kakaoAuthApi.getAccessToken(
                clientId,
                clientSecret,
                "authorization_code",
                redirectUrl,
                authorizationCode
        );

        log.info("Kakao auth response status: {}", response.getStatusCode());
        log.info("Kakao auth response body: {}", response.getBody());

        if (response.getBody() == null) {
            log.error("Received empty response from Kakao auth API.");
            throw new AppException(ExceptionCode.TOKEN_EXPIRED);
        }

        try {
            SocialAuthResponse authResponse = new Gson().fromJson(String.valueOf(response.getBody()), SocialAuthResponse.class);

            return authResponse.accessToken();
        } catch (JsonSyntaxException e) {
            log.error("Error deserializing Kakao auth response: {}", e.getMessage());
            throw new AppException(ExceptionCode.TOKEN_EXPIRED);
        }
    }

    public SocialUserResponse getUserInfo(String accessToken) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);

        ResponseEntity<?> response = kakaoUserApi.getUserInfo(headerMap);

        log.info("Kakao user response: {}", response.toString());

        if (response.getBody() == null) {
            log.error("Received empty response from Kakao user API.");
            throw new AppException(ExceptionCode.NOT_FOUND_USER);
        }

        String jsonString = response.getBody().toString();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();

        KaKaoLoginResponse kaKaoLoginResponse = gson.fromJson(jsonString, KaKaoLoginResponse.class);
        KaKaoLoginResponse.KakaoLoginData kakaoLoginData = kaKaoLoginResponse.kakao_account();

        String nickname = Optional.ofNullable(kakaoLoginData.nickname())
                .orElse("default_nickname");
        String email = Optional.ofNullable(kakaoLoginData.email())
                .orElse("default_email@example.com");

        return SocialUserResponse.builder()
                .nickname(nickname)
                .email(email)
                .build();
    }
}

