package com.fesi.mukitlist.api.controller.auth.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String accessToken
) {
    public static AuthenticationResponse of(String accessToken) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
