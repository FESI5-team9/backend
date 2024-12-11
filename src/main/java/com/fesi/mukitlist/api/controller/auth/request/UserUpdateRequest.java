package com.fesi.mukitlist.api.controller.auth.request;

import org.springframework.web.multipart.MultipartFile;

import com.fesi.mukitlist.domain.service.auth.request.UserUpdateServiceRequest;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateRequest(
	String nickname,
	@Schema(description = "프로필 이미지", type = "string", format = "binary")
	MultipartFile image
) {
	public static UserUpdateServiceRequest toServiceRequest(String nickname, MultipartFile image) {
		return UserUpdateServiceRequest.of(nickname, image);
	}
}
