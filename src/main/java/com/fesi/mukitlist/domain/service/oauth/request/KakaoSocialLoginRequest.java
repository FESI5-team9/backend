package com.fesi.mukitlist.domain.service.oauth.request;

import com.fesi.mukitlist.core.auth.constant.UserType;

import jakarta.validation.constraints.NotNull;

public record KakaoSocialLoginRequest(
        @NotNull UserType userType,
        @NotNull String code
) {
}