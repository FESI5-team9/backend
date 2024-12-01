package com.fesi.mukitlist.api.service.auth.response;

import com.fesi.mukitlist.domain.auth.User;

public record UserResponse(
	Long id,
	String nickname,
	String image
) {
	public static UserResponse of(User user) {
		return new UserResponse(user.getId(), user.getNickname(), user.getImage());
	}
}
