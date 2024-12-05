package com.fesi.mukitlist.api.controller.auth.request;

import com.fesi.mukitlist.api.service.auth.request.TokenServiceRequest;
import com.fesi.mukitlist.domain.auth.constant.GrantType;
import com.fesi.mukitlist.domain.auth.constant.TokenType;
import com.fesi.mukitlist.domain.auth.User;
import lombok.Builder;

import java.io.Serializable;

public record TokenRequest(
        Long id,
        String token,
        GrantType grantType,
        TokenType tokenType,
        boolean expired,
        User user
) implements Serializable {
    public static TokenRequest of(Long id, String token, GrantType grantType, TokenType tokenType, boolean expired, User user) {
        return new TokenRequest(id, token, grantType,tokenType, expired, user);
    }

    @Builder
    public TokenServiceRequest toServiceRequest() {
        return TokenServiceRequest.builder()
                .id(id)
                .token(token)
                .grantType(grantType)
                .tokenType(tokenType)
                .expired(expired)
                .build();
    }
}
