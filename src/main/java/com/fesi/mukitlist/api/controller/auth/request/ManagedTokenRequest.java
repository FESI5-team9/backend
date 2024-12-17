package com.fesi.mukitlist.api.controller.auth.request;

public record ManagedTokenRequest(
	String refreshToken
) {
	public static ManagedTokenRequest of(String refreshToken) {
		return new ManagedTokenRequest(refreshToken);
	}
}
