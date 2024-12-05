package com.fesi.mukitlist.api.controller.auth.request;

import com.fesi.mukitlist.api.service.auth.request.TokenServiceRequest;
import com.fesi.mukitlist.domain.auth.TokenType;
import com.fesi.mukitlist.domain.auth.User;
import lombok.Builder;

import java.io.Serializable;

public record TokenRequest(
        Long id,
        String token,
        TokenType tokenType,
        boolean expired,
        User user
) implements Serializable {
    public static TokenRequest of(Long id, String token, TokenType tokenType, boolean expired, User user) {
        return new TokenRequest(id, token, tokenType, expired, user);
    }

    @Builder
    public TokenServiceRequest toServiceRequest() {
        return TokenServiceRequest.builder()
                .id(id)
                .token(token)
                .tokenType(tokenType)
                .expired(expired)
                .build();
    }
}
