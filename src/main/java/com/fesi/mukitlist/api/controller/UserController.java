package com.fesi.mukitlist.api.controller;

import com.fesi.mukitlist.api.service.UserService;
import com.fesi.mukitlist.api.controller.dto.request.UserCreateRequest;
import com.fesi.mukitlist.exception.EmailExistedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserCreateRequest userCreateRequest) throws EmailExistedException {
        try {
            userService.registerUser(
                    userCreateRequest.email(),
                    userCreateRequest.name(),
                    userCreateRequest.password(),
                    userCreateRequest.companyName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("사용자 생성 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
