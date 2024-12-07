package com.fesi.mukitlist.api.controller.oauth.kakao.request;

import lombok.Builder;

@Builder
public record KakaoTokenRequest(
        String grant_type,
        String client_id,
        String redirect_uri,
        String code,
        String client_secret
) {

}