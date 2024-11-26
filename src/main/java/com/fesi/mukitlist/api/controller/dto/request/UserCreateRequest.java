package com.fesi.mukitlist.api.controller.dto.request;

import lombok.Builder;

/**
 * DTO for creating a new {@link com.fesi.mukitlist.api.domain.User}
 */
@Builder
public record UserCreateRequest(
        String email,
        String password,
        String name,
        String companyName,
        String image
) {

    public UserCreateRequest toServiceRequest() {
        return UserCreateRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .companyName(companyName)
                .image(image)
                .build();
    }
}