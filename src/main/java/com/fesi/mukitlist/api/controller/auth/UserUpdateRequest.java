package com.fesi.mukitlist.api.controller.auth;

import com.fesi.mukitlist.api.service.auth.request.UserUpdateServiceRequest;

public record UserUpdateRequest(
	String nickname
) {
	public static UserUpdateServiceRequest toServiceRequest(String nickname) {
		return UserUpdateServiceRequest.of(nickname);
	}
}
