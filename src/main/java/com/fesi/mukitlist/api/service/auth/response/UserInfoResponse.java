package com.fesi.mukitlist.api.service.auth.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.auth.User;

public record UserInfoResponse(
	Long id,
	String email,
	String name,
	String nickname,
	String image,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static UserInfoResponse of(User user) {
		return new UserInfoResponse(user.getId(), user.getEmail(), user.getName(), user.getNickname(), user.getImage(),
			user.getCreatedAt(), user.getUpdatedAt());
	}
}
