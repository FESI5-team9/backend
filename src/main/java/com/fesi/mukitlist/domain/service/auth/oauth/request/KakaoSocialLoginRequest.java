package com.fesi.mukitlist.domain.service.auth.oauth.request;

import com.fesi.mukitlist.core.auth.application.constant.UserType;

import jakarta.validation.constraints.NotNull;

public record KakaoSocialLoginRequest(
        @NotNull UserType userType,
        @NotNull String code
) {
}