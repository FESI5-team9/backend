package com.fesi.mukitlist.api.service.request;

import lombok.Builder;

@Builder
public record UserServiceCreateRequest(
        String email,
        String password,
        String name,
        String companyName,
        String image
) {
}