package com.fesi.mukitlist.domain.service.auth.oauth.response;

import lombok.Builder;

@Builder
public record SocialUserResponse(
        String email,
        String nickname
) {
    public static SocialUserResponse fromKaKaoLoginResponse(
        KaKaoLoginResponse kaKaoLoginResponse) {
        KaKaoLoginResponse.KakaoLoginData kakaoLoginData = kaKaoLoginResponse.kakao_account();
        return SocialUserResponse.builder()
                .email(kakaoLoginData.email())
                .nickname(kakaoLoginData.nickname())
                .build();
    }
}
