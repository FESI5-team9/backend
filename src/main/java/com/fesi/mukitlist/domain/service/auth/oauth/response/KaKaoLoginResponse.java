package com.fesi.mukitlist.domain.service.auth.oauth.response;

public record KaKaoLoginResponse(
        KakaoLoginData kakao_account
) {
    public record KakaoLoginData(
            String email,
            String nickname
    ) {}
}
