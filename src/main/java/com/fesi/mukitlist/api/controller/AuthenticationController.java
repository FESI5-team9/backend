package com.fesi.mukitlist.api.controller;

import com.fesi.mukitlist.api.controller.dto.request.AuthenticationRequest;
import com.fesi.mukitlist.api.controller.dto.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.dto.response.AuthenticationResponse;
import com.fesi.mukitlist.api.service.AuthenticationService;
import com.fesi.mukitlist.api.service.request.AuthenticationServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signin") // TODO 403 Forbidden ERROR
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserCreateRequest request
    ) {
        return ResponseEntity.ok(service.register(request.toServiceRequest()));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationServiceRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
