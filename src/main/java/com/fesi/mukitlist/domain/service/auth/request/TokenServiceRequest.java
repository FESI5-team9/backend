package com.fesi.mukitlist.domain.service.auth.request;

import com.fesi.mukitlist.core.auth.GrantType;
import com.fesi.mukitlist.core.auth.TokenType;
import com.fesi.mukitlist.core.auth.application.User;
import lombok.Builder;

@Builder
public record TokenServiceRequest(
        Long id,
        String token,
        GrantType grantType,
        TokenType tokenType,
        boolean expired,
        User user
) {
    public TokenServiceRequest withUpdatedStatus(boolean expired) {
        return new TokenServiceRequest(
                this.id,
                this.token,
                this.grantType,
                this.tokenType,
                expired,
                this.user
        );
    }
}
