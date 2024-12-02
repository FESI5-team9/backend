package com.fesi.mukitlist.api.service.auth.request;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateServiceRequest(
	String nickname,
	MultipartFile image
) {
	public static UserUpdateServiceRequest of(String nickname, MultipartFile image) {
		return new UserUpdateServiceRequest(nickname,image);
	}
}
