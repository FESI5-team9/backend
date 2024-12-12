package com.fesi.mukitlist.domain.service.auth.oauth.response;

public record SocialAuthResponse(
        String accessToken,
        String tokenType,
        String refreshToken,
        String expiresIn,
        String scope,
        String refreshTokenExpiresIn
) {

}