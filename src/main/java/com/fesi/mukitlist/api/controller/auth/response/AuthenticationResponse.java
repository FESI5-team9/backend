package com.fesi.mukitlist.api.controller.auth.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
    public static AuthenticationResponse of(String token) {
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}