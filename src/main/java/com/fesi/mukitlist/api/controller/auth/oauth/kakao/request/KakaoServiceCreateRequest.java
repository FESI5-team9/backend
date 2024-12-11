package com.fesi.mukitlist.api.controller.auth.oauth.kakao.request;

import com.fesi.mukitlist.domain.auth.constant.UserType;


public record KakaoServiceCreateRequest(
	String email,
	String nickname,
	UserType userType,
	String providerId
) {

}