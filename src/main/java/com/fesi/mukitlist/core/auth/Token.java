package com.fesi.mukitlist.core.auth;

import com.fesi.mukitlist.core.auth.application.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public GrantType grantType;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @Builder
    private Token(String token, GrantType grantType, TokenType tokenType, boolean expired, User user) {
        this.token = token;
        this.grantType = grantType;
        this.tokenType = tokenType;
        this.expired = expired;
        this.user = user;
    }

    public static Token of(String token, GrantType grantType, TokenType tokenType, boolean expired, User user) {
        return Token.builder()
            .token(token)
            .grantType(grantType)
            .tokenType(tokenType)
            .expired(expired)
            .user(user)
            .build();
    }

}