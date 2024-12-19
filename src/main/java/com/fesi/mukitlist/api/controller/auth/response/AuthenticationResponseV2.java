package com.fesi.mukitlist.api.controller.auth.response;

import lombok.Builder;

@Builder
public record AuthenticationResponseV2(
	String accessToken,
	String refreshToken
) {
	public static AuthenticationResponseV2 of(String accessToken, String refreshToken) {
		return AuthenticationResponseV2.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
