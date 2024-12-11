package com.fesi.mukitlist.api.service.oauth.request;

import com.fesi.mukitlist.domain.auth.constant.UserType;
import jakarta.validation.constraints.NotNull;

public record KakaoSocialLoginRequest(
        @NotNull UserType userType,
        @NotNull String code
) {
}