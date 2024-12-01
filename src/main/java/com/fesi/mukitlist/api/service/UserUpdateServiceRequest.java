package com.fesi.mukitlist.api.service;

import com.fesi.mukitlist.api.controller.auth.UserUpdateRequest;

public record UserUpdateServiceRequest(
	String nickname
) {
	public static UserUpdateServiceRequest of(String nickname) {
		return new UserUpdateServiceRequest(nickname);
	}
}
