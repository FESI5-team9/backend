package com.fesi.mukitlist.api.controller.auth;

import com.fesi.mukitlist.api.service.auth.response.AuthenticationServiceResponse;
import com.fesi.mukitlist.api.service.auth.AuthenticationService;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<Map<String,String>> authenticate(
            @RequestBody AuthenticationServiceRequest request) {
        AuthenticationServiceResponse authenticate = authenticationService.authenticate(request);
        return new ResponseEntity<>(Map.of("message","로그인 성공"), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String,String>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
        return new ResponseEntity<>(Map.of("message","로그인 성공"), HttpStatus.OK);
    }
}

