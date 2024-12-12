package com.fesi.mukitlist.api.controller.auth.oauth.kakao.request;

import com.fesi.mukitlist.core.auth.constant.UserType;

public record KakaoUserCreateRequest(
	String email,
	String nickname,
	UserType provider,
	String providerId
) {

	public static KakaoServiceCreateRequest toServiceRequest(String email, String nickname, UserType provider, String providerId) {
		return new KakaoServiceCreateRequest(email, nickname, provider, providerId);
	}
}