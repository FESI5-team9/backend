package com.fesi.mukitlist.domain.service.auth.request;

import lombok.Builder;

@Builder
public record UserServiceCreateRequest(
        String email,
        String password,
        String nickname
        // String image
) {
}
