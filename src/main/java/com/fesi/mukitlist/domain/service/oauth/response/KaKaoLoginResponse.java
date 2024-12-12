package com.fesi.mukitlist.domain.service.oauth.response;

public record KaKaoLoginResponse(
        KakaoLoginData kakao_account
) {
    public record KakaoLoginData(
            String email,
            String nickname
    ) {}
}
