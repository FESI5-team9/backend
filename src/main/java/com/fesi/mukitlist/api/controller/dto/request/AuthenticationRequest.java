package com.fesi.mukitlist.api.controller.dto.request;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record AuthenticationRequest(
        String email,
        String password
) implements Serializable {


    public AuthenticationRequest toServiceRequest() {
        return AuthenticationRequest.builder()  // Return AuthenticationRequest instead of AuthenticationServiceRequest
                .email(email)
                .password(password)
                .build();
    }
}
