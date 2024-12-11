package com.fesi.mukitlist.api.service.oauth;

import com.fesi.mukitlist.api.service.oauth.response.SocialUserResponse;
import com.fesi.mukitlist.domain.auth.constant.UserType;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {

    UserType getServiceName();
    String getAccessToken(String authorizationCode);
    SocialUserResponse getUserInfo(String accessToken);

}
