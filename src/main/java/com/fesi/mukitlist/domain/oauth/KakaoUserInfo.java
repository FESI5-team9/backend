package com.fesi.mukitlist.domain.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

	private String id;
	private Map<String, Map<String, Object>> kakaoAccount;

	public KakaoUserInfo(Map<String, Map<String, Object>> kakaoAccount, String id) {
		this.kakaoAccount = kakaoAccount;
		this.id = id;
	}

	@Override
	public String getProviderId() {
		return id;
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		return String.valueOf(kakaoAccount.get("email"));
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getNickname() {
		return kakaoAccount.get("profile").get("nickname").toString();
	}
}
