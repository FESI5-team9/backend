package com.fesi.mukitlist.api.controller.web.response;

import lombok.Builder;

@Builder
public record KakaoTokenResponse(
        String token_type,
        String access_token,
        Integer expires_in,
        String refresh_token,
        Integer refresh_token_expires_in,
        String scope
) {

}