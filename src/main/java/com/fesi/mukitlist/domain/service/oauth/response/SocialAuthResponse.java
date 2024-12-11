package com.fesi.mukitlist.api.service.oauth.response;

public record SocialAuthResponse(
        String accessToken,
        String tokenType,
        String refreshToken,
        String expiresIn,
        String scope,
        String refreshTokenExpiresIn
) {

}