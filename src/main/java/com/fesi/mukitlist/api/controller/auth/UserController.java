package com.fesi.mukitlist.api.controller.auth;

import java.security.Principal;
import java.util.Map;

import com.fesi.mukitlist.api.service.auth.UserService;
import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.service.auth.response.UserInfoResponse;
import com.fesi.mukitlist.api.service.auth.response.UserResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.global.annotation.Authorize;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<Map<String, String>> signup(
		@RequestBody UserCreateRequest userCreateRequest) {
		userService.createUser(userCreateRequest.toServiceRequest());
		return new ResponseEntity<>(Map.of("message", "사용자 생성 성공"), HttpStatus.CREATED);
	}

	@GetMapping("user")
	public ResponseEntity<UserInfoResponse> getUser(@Authorize User user) {
		return new ResponseEntity<>(userService.getUserInfo(user.getId()), HttpStatus.OK);
	}

	@PutMapping("user")
	public ResponseEntity<UserInfoResponse> updateUser(
        @Authorize User user,
        @RequestPart("request") UserUpdateRequest request,
		@RequestPart(value = "image", required = false)
		MultipartFile image) {
        UserInfoResponse response = userService.updateUser(user.getId(), request, image);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
