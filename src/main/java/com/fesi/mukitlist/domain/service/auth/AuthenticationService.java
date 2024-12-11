package com.fesi.mukitlist.domain.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.core.auth.User;
import com.fesi.mukitlist.core.auth.constant.GrantType;
import com.fesi.mukitlist.core.auth.constant.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.repository.TokenRepository;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.domain.service.auth.request.AuthenticationServiceRequest;
import com.fesi.mukitlist.core.auth.Token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.fesi.mukitlist.api.exception.ExceptionCode.NOT_FOUND_USER;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private void saveUserToken(PrincipalDetails principalDetails, String refreshToken) {
        Token existingToken = tokenRepository.findByUserAndToken(principalDetails.getUser().getId(), refreshToken);
        if (existingToken != null) {
            existingToken = Token.builder()
                    .id(existingToken.getId())
                    .user(principalDetails.getUser())
                    .token(refreshToken)
                    .grantType(GrantType.BEARER)
                    .tokenType(TokenType.REFRESH)
                    .expired(false)
                    .build();
            tokenRepository.save(existingToken);
        } else {
            Token token = Token.builder()
                    .user(principalDetails.getUser())
                    .token(refreshToken)
                    .tokenType(TokenType.REFRESH)
                    .expired(false)
                    .build();
            tokenRepository.save(token);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationServiceRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(NOT_FOUND_USER));

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        String accessToken = jwtService.generateToken(principalDetails);
        String refreshToken = jwtService.generateRefreshToken(principalDetails);
        saveUserToken(principalDetails, refreshToken);
        addRefreshTokenToCookie(response, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            User findUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new AppException(NOT_FOUND_USER));
            if (!jwtService.isRefreshTokenValid(refreshToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
            PrincipalDetails principalDetails = new PrincipalDetails(findUser);
            String accessToken = jwtService.generateToken(principalDetails);
            saveUserToken(principalDetails, refreshToken);
            addRefreshTokenToCookie(response, refreshToken);
            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh-token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);
    }
}