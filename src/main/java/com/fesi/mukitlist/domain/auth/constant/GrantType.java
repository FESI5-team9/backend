package com.fesi.mukitlist.domain.auth.constant;

public enum GrantType {

    BEARER("Bearer"),
    AUTHORIZATION("authorization_code");

    GrantType(final String type) {
        this.type = type;
    }

    private String type;
}
