package com.fesi.mukitlist.api.service.auth.request;

import com.fesi.mukitlist.domain.auth.TokenType;
import com.fesi.mukitlist.domain.auth.User;
import lombok.Builder;

@Builder
public record TokenServiceRequest(
        Long id,
        String token,
        TokenType tokenType,
        boolean expired,
        User user
) {
    public TokenServiceRequest withUpdatedStatus(boolean expired) {
        return new TokenServiceRequest(
                this.id,
                this.token,
                this.tokenType,
                expired,
                this.user
        );
    }
}
