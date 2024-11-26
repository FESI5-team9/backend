package com.fesi.mukitlist.api.controller.auth;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.service.auth.AuthenticationService;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;
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
