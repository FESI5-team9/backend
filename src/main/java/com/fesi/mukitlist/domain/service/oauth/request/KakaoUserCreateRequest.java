package com.fesi.mukitlist.domain.service.oauth.request;

import com.fesi.mukitlist.core.auth.constant.UserType;

public record KakaoUserCreateRequest(
        String email,
        String nickname,
        String password,
        UserType userType
) {
}
