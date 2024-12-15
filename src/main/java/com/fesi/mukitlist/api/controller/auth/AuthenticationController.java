package com.fesi.mukitlist.api.controller.auth;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.response.SimpleApiResponse;
import com.fesi.mukitlist.domain.service.auth.AuthenticationService;
import com.fesi.mukitlist.domain.service.auth.application.UserService;
import com.fesi.mukitlist.domain.service.auth.request.AuthenticationServiceRequest;
import com.fesi.mukitlist.domain.service.auth.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final UserService userService;

	@Value("${kakao.client.id}")
	private String clientId;

	@Value("${kakao.client.redirect_url}")
	private String redirectUri;

	@Operation(summary = "회원가입", description = "회원 가입을 진행합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "회원 가입 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = UserInfoResponse.class))),
			@ApiResponse(
				responseCode = "400",
				description = "타입 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"VALIDATION_ERROR\", \"parameter\":\"email\", \"message\":\"이메일 양식을 지켜주세요.\"}"
					)
				)
			),
		}
	)
	@PostMapping("/signup")
	public ResponseEntity<SimpleApiResponse> signup(
		@RequestBody UserCreateRequest userCreateRequest) {
		userService.createUser(userCreateRequest.toServiceRequest());
		return new ResponseEntity<>(SimpleApiResponse.of("사용자 생성 성공"), HttpStatus.CREATED);
	}

	@Operation(summary = "로그인", description = "로그인을 시도합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "로그인 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = AuthenticationResponse.class))),
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
	@PostMapping("/signin")
	public ResponseEntity<AuthenticationResponse> signIn(
		@RequestBody AuthenticationServiceRequest request, HttpServletResponse response) {
		return ResponseEntity.ok(authenticationService.authenticate(request, response));
	}

	@Operation(
		summary = "카카오 로그인 리다이렉트",
		description = "카카오 로그인 리다이렉트를 시도합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "로그인 페이지로 리다이렉트됨",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"message\":\"카카오 로그인 페이지로 리다이렉트됩니다. URL을 복사하여 브라우저에 입력하세요.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "잘못된 요청",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"BAD_REQUEST\",\"message\":\"잘못된 요청입니다.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"INTERNAL_SERVER_ERROR\",\"message\":\"서버 오류가 발생했습니다.\"}"
					)
				)
			)
		}
	)
	@GetMapping("/kakao/login")
	public ResponseEntity<Map<String, String>> redirectToKakaoLogin() {
		String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId +
			"&redirect_uri=" + redirectUri + "&response_type=code";

		Map<String, String> response = new HashMap<>();
		response.put("message", "카카오 로그인 페이지로 리다이렉트됩니다. URL을 복사하여 브라우저에 입력하세요.");
		response.put("redirectUrl", kakaoAuthUrl);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "이메일 중복확인", description = "이메일 중복 확인을 진행합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "중복 확인 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = SimpleApiResponse.class))),
			@ApiResponse(
				responseCode = "400",
				description = "타입 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"VALIDATION_ERROR\", \"parameter\":\"email\", \"message\":\"이메일 양식을 지켜주세요.\"}"
					)
				)
			),
		}
	)
	@GetMapping("/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmailDuplicated(@RequestParam String email) {
		return new ResponseEntity(SimpleApiResponse.of(String.valueOf(userService.checkEmail(email))), HttpStatus.OK);
	}

	@Operation(summary = "닉네임 중복확인", description = "닉네임 중복 확인을 진행합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "중복 확인 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = SimpleApiResponse.class))),
		}
	)
	@GetMapping("/check-nickname")
	public ResponseEntity<Map<String, Boolean>> checkNicknameDuplicated(@RequestParam String nickname) {
		return new ResponseEntity(SimpleApiResponse.of(String.valueOf(userService.checkNickname(nickname))),
			HttpStatus.OK);
	}

	@Operation(
		summary = "새로운 액세스 토큰 발급",
		description = "쿠키에서 리프레시 토큰을 가져와 유효성을 검사한 후 새로운 액세스 토큰을 발급합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "새로운 액세스 토큰이 발급되었습니다.",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = AuthenticationResponse.class)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "잘못된 요청 또는 쿠키에 리프레시 토큰이 없음",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"BAD_REQUEST\",\"message\":\"쿠키에 리프레시 토큰이 없습니다.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "401",
				description = "유효하지 않거나 만료된 리프레시 토큰",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"TOKEN_EXPIRED\",\"message\":\"유효하지 않은 리프레시 토큰입니다.\"}"
					)
				)
			)
		}
	)
	@PostMapping("/managed-access-token") // TODO 더 좋게 받을 방법 있을까 고민
	public ResponseEntity<AuthenticationResponse> refreshToken(
		@Parameter(hidden = true)
		@CookieValue(value = "refresh-token") String refreshToken) throws IOException {
		return ResponseEntity.ok(
			AuthenticationResponse.of(authenticationService.generateToNewAccessToken(refreshToken)));
	}

}
