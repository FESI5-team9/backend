package com.fesi.mukitlist.domain.service.auth.oauth.request;

import com.fesi.mukitlist.core.auth.application.constant.UserType;

public record KakaoUserCreateRequest(
        String email,
        String nickname,
        String password,
        UserType userType
) {
}
