package com.fesi.mukitlist.api.service.auth.request;

import com.fesi.mukitlist.domain.auth.constant.GrantType;
import com.fesi.mukitlist.domain.auth.constant.TokenType;
import com.fesi.mukitlist.domain.auth.User;
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
