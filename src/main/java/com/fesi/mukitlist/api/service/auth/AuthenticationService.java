package com.fesi.mukitlist.api.service.auth;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.domain.auth.Token;
import com.fesi.mukitlist.domain.auth.TokenType;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.api.repository.TokenRepository;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .build();
        tokenRepository.save(token);

        log.info("Token saved:: {}", token.getToken());
    }

    public AuthenticationResponse authenticate(AuthenticationServiceRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Token existingToken = tokenRepository.findByUser(user)
                .filter(t -> !t.isExpired()) // 만료되지 않은 토큰을 찾습니다.
                .orElse(null);

        String jwtToken;
        if (existingToken != null) {
            jwtToken = existingToken.getToken();
        } else {
            jwtToken = jwtService.generateToken(user);  // 새 토큰 생성
            saveUserToken(user, jwtToken);  // 새 토큰 저장
        }

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}