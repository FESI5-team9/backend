package com.fesi.mukitlist.api.controller.auth.request;

import com.fesi.mukitlist.api.service.auth.request.UserServiceCreateRequest;
import com.fesi.mukitlist.domain.auth.User;

import lombok.Builder;

/**
 * DTO for creating a new {@link User}
 */
@Builder
public record UserCreateRequest(
	String email,
	String password,
	String nickname,
	String name
	// String image
) {

	public UserServiceCreateRequest toServiceRequest() {
		return UserServiceCreateRequest.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.name(name)
			.build();
	}
}