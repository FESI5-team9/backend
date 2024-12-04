package com.fesi.mukitlist.api.controller.web;

import com.fesi.mukitlist.api.controller.web.client.KakaoTokenClient;
import com.fesi.mukitlist.api.controller.web.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KakaoTokenController {

    private final KakaoTokenClient kakaoTokenClient;

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
    public KakaoTokenResponse loginCallback(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=UTF-8";

        KakaoTokenResponse kakaoTokenResponse = kakaoTokenClient.requestKakaoToken(
                "authorization_code",
                clientId,
                clientSecret,
                code,
                "http://localhost:8080/api/auth/kakao/callback"
        );

        System.out.println("Kakao Token Response: " + kakaoTokenResponse);

        return kakaoTokenResponse;
    }
}
