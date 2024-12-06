package com.fesi.mukitlist.api.service.auth;

import com.fesi.mukitlist.api.repository.TokenRepository;
import com.fesi.mukitlist.domain.auth.Token;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.auth.constant.GrantType;
import com.fesi.mukitlist.domain.auth.constant.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveUserToken(User user, String refreshToken) {
        Token existingToken = tokenRepository.findByUserAndToken(user, refreshToken);
        if (existingToken != null) {
            existingToken = Token.builder()
                    .id(existingToken.getId())
                    .user(user)
                    .token(refreshToken)
                    .grantType(GrantType.BEARER)
                    .tokenType(TokenType.REFRESH)
                    .expired(false)
                    .build();
            tokenRepository.save(existingToken);
        } else {
            Token token = Token.builder()
                    .user(user)
                    .token(refreshToken)
                    .grantType(GrantType.BEARER)
                    .tokenType(TokenType.REFRESH)
                    .expired(false)
                    .build();
            tokenRepository.save(token);
        }
    }
}

