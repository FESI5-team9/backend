package com.fesi.mukitlist.domain.auth.constant;

import lombok.Getter;

@Getter
public enum UserType {
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE"),
    NORMAL("DEFAULT");

    private final String providerName;
    UserType(String providerName) {
        this.providerName = providerName;
    }
}
