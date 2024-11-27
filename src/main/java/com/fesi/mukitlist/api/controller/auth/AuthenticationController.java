package com.fesi.mukitlist.api.controller.auth;

import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.service.auth.AuthenticationService;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<Map<String,String>> authenticate(
            @RequestBody AuthenticationServiceRequest request) {
        AuthenticationResponse authenticate = authenticationService.authenticate(request);
        return new ResponseEntity<>(Map.of("message","로그인 성공"), HttpStatus.OK);
    }
}

