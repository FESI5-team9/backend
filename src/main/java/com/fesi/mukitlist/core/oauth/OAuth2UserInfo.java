package com.fesi.mukitlist.core.oauth;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getNickname();

}


