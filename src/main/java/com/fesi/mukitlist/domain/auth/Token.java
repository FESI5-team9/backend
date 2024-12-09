package com.fesi.mukitlist.domain.auth;

import com.fesi.mukitlist.domain.auth.constant.GrantType;
import com.fesi.mukitlist.domain.auth.constant.TokenType;
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
    private Long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private GrantType grantType;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Token(Long id, String token, GrantType grantType, TokenType tokenType, boolean expired, User user) {
        this.id = id;
        this.token = token;
        this.grantType = grantType;
        this.tokenType = tokenType;
        this.expired = expired;
        this.user = user;
    }
}