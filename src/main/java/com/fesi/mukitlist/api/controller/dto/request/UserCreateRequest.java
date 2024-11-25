package com.fesi.mukitlist.api.controller.dto.request;

import com.fesi.mukitlist.api.service.request.UserServiceCreateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * DTO for creating a new {@link com.fesi.mukitlist.api.domain.User}
 */
public record UserCreateRequest(
        String email,
        String password,
        String name,
        String companyName,
        String image
) {
    public UserServiceCreateRequest toServiceRequest() {

        return UserServiceCreateRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .companyName(companyName)
                .image(image)
                .build();
    }
}