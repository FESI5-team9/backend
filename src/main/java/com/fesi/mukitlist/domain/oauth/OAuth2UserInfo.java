package com.fesi.mukitlist.domain.oauth;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getNickname();

}


