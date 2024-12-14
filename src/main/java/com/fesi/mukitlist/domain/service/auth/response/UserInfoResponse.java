package com.fesi.mukitlist.domain.service.auth.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.core.auth.application.User;

public record UserInfoResponse(
	Long id,
	String email,
	String nickname,
	String image,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static UserInfoResponse of(User user) {
		return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname(), user.getImage(),
			user.getCreatedAt(), user.getUpdatedAt());
	}
}
