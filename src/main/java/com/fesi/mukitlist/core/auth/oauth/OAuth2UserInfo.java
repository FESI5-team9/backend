package com.fesi.mukitlist.core.auth.oauth;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getNickname();

}


