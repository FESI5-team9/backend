package com.fesi.mukitlist.api.controller.auth.request;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record AuthenticationRequest(
        String email,
        String password
) implements Serializable {

    public AuthenticationRequest toServiceRequest() {
        return AuthenticationRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
