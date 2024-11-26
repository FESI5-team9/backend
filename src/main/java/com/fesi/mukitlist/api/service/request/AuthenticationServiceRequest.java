package com.fesi.mukitlist.api.service.request;

import lombok.Builder;

@Builder
public record AuthenticationServiceRequest(
        String email,
        String password
) {

}
