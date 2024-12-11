package com.fesi.mukitlist.api.service.oauth;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.exception.ExceptionCode;
import com.fesi.mukitlist.api.service.auth.UserService;
import com.fesi.mukitlist.api.service.oauth.response.LoginResponse;
import com.fesi.mukitlist.api.service.oauth.response.SocialUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SocialLoginController {

    private final UserService userService;
    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/social-login")
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestParam("access_token") String accessToken) {
        try {
            SocialUserResponse userResponse = kakaoLoginService.getUserInfo(accessToken);

            LoginResponse loginResponse = userService.doSocialLogin(String.valueOf(userResponse));

            return ResponseEntity.ok(loginResponse);

        } catch (AppException e) {
            return ResponseEntity.status(e.getExceptionCode().getStatus())
                    .body(new LoginResponse(e.getExceptionCode().getCode(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(ExceptionCode.IO_EXCEPTION.getCode(), "An unknown error occurred"));
        }
    }
}
