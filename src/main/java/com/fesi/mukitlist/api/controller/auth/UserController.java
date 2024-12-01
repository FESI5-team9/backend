package com.fesi.mukitlist.api.controller.auth;

import java.security.Principal;
import java.util.Map;

import com.fesi.mukitlist.api.service.auth.UserService;
import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.domain.auth.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> signup(
            @RequestBody UserCreateRequest userCreateRequest) {
            userService.createUser(userCreateRequest.toServiceRequest());
        return new ResponseEntity<>(Map.of("message","사용자 생성 성공"), HttpStatus.CREATED);
    }
}
