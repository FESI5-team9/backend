package com.fesi.mukitlist.api.controller.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fesi.mukitlist.api.controller.auth.request.UserUpdateRequest;
import com.fesi.mukitlist.domain.service.auth.application.UserService;
import com.fesi.mukitlist.domain.service.auth.response.UserInfoResponse;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.api.controller.annotation.Authorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "Auth", description = "인증 관련 API")
public class UserController {

	private final UserService userService;
	@Operation(summary = "유저 정보 조회", description = "유저 정보를 확인 합니다.",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(responseCode = "200", description = "유저 정보 조회 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = UserInfoResponse.class))),
			@ApiResponse(
				responseCode = "401",
				description = "인증 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"LOGIN_REQUIRED\",\"message\":\"로그인이 필요합니다.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "403",
				description = "권한 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"FORBIDDEN\",\"message\":\"권한이 없습니다.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "유저 없음",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"NOT_FOUND\",\"message\":\"유저를 찾을 수 없습니다.\"}"
					)
				)
			),
		}
	)
	@GetMapping
	public ResponseEntity<UserInfoResponse> getUser(@Parameter(hidden = true) @Authorize PrincipalDetails user) {
		return new ResponseEntity<>(userService.getUserInfo(user.getUser()), HttpStatus.OK);
	}

	@Operation(summary = "유저 정보 수정", description = "유저 정보를 수정 합니다.",
		security = @SecurityRequirement(name = "bearerAuth"),
		responses = {
			@ApiResponse(responseCode = "200", description = "유저 정보 수정 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = UserInfoResponse.class))),
			@ApiResponse(
				responseCode = "401",
				description = "인증 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"LOGIN_REQUIRED\",\"message\":\"로그인이 필요합니다.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "403",
				description = "권한 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"FORBIDDEN\",\"message\":\"권한이 없습니다.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "유저 없음",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"NOT_FOUND\",\"message\":\"유저를 찾을 수 없습니다.\"}"
					)
				)
			),
		}
	)
	@PutMapping(consumes = "multipart/form-data")
	public ResponseEntity<UserInfoResponse> updateUser(
		@Parameter(hidden = true) @Authorize PrincipalDetails user,
		@ModelAttribute UserUpdateRequest request) throws IOException {
		UserInfoResponse response = userService.updateUser(user.getUser(), request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
