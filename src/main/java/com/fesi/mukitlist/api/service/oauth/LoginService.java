package com.fesi.mukitlist.api.service.oauth;

import com.fesi.mukitlist.api.service.oauth.response.SocialUserResponse;
import com.fesi.mukitlist.domain.auth.constant.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Component
@Qualifier("defaultLoginService")
public class LoginService implements SocialLoginService{

    @Override
    public UserType getServiceName() {
        return UserType.NORMAL;
    }

    @Override
    public String getAccessToken(String authorizationCode) {
        return null;
    }

    @Override
    public SocialUserResponse getUserInfo(String accessToken) {
        return null;
    }

}
