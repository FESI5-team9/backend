package com.fesi.mukitlist.api.service.auth.request;

public record UserUpdateServiceRequest(
	String nickname
) {
	public static UserUpdateServiceRequest of(String nickname) {
		return new UserUpdateServiceRequest(nickname);
	}
}
