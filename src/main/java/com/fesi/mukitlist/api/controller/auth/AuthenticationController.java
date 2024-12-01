package com.fesi.mukitlist.api.controller.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.service.auth.AuthenticationService;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationServiceRequest request) {
        AuthenticationResponse authenticate = authenticationService.authenticate(request);
        return new ResponseEntity<>(authenticate, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}

