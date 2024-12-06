package com.fesi.mukitlist.api.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.domain.auth.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;
import com.fesi.mukitlist.domain.auth.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.fesi.mukitlist.api.exception.ExceptionCode.NOT_FOUND_USER;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationServiceRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(NOT_FOUND_USER));
        PrincipalDetails principalDetails = createPrincipalDetails(user);
        return generateAndSaveTokensIn(principalDetails, user);

    }

    private PrincipalDetails createPrincipalDetails(User user) {
        return new PrincipalDetails(user);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow(() -> new AppException(NOT_FOUND_USER));

            PrincipalDetails principalDetails = createPrincipalDetails(user);
            if (!jwtService.isTokenValid(refreshToken, principalDetails)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            AuthenticationResponse authResponse = generateAndSaveTokensIn(principalDetails, user);
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private AuthenticationResponse generateAndSaveTokensIn(PrincipalDetails principalDetails, User user) {
        String accessToken = jwtService.generateToken(principalDetails);
        String refreshToken = jwtService.generateRefreshToken(principalDetails);

        tokenService.saveUserToken(user, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}