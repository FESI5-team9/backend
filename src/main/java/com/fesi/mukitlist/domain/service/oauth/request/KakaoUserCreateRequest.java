package com.fesi.mukitlist.api.service.oauth.request;

import com.fesi.mukitlist.domain.auth.constant.UserType;

public record KakaoUserCreateRequest(
        String email,
        String nickname,
        String password,
        UserType userType
) {
}
