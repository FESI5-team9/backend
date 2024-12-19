package com.fesi.mukitlist.domain.service.auth.response;

import com.fesi.mukitlist.core.auth.application.User;

public record UserResponse(
	Long id,
	String nickname,
	String image
) {
	public static UserResponse of(User user) {
		return new UserResponse(user.getId(), user.getNickname(), user.getImage());
	}
}
