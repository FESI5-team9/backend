package com.fesi.mukitlist.api.controller.auth.response;

import lombok.Builder;

@Builder
public record UserCreateResponse(
        String email,
        String nickname,
        String name
) {
    public static UserCreateResponse of(String email, String nickname, String name) {
        return UserCreateResponse.builder()
                .email(email)
                .nickname(nickname)
                .name(name)
                .build();
    }
}
