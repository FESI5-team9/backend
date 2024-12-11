package com.fesi.mukitlist.domain.service.auth.request;

import lombok.Builder;

@Builder
public record AuthenticationServiceRequest(
        String email,
        String password
) {

}
