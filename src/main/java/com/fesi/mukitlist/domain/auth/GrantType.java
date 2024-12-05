package com.fesi.mukitlist.domain.auth;

public enum GrantType {

    BEARER("Bearer");

    GrantType(final String type) {
        this.type = type;
    }

    private String type;
}
