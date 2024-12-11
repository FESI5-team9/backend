package com.fesi.mukitlist.domain.service.oauth;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.domain.service.auth.UserService;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.core.auth.User;
import com.fesi.mukitlist.core.oauth.KakaoUserInfo;
import com.fesi.mukitlist.core.oauth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(
                    (Map)oAuth2User.getAttributes().get("kakao_account"),
                    String.valueOf(oAuth2User.getAttributes().get("id")));
        } else {
            log.info("지원하지 않는 서비스 입니다."); // TODO Http status, Error Code 정의
        }

        String provider = oAuth2User.getAttributes().get("provider").toString();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = passwordEncoder.encode("테스트");
        String nickname = oAuth2UserInfo.getNickname() != null ? oAuth2UserInfo.getNickname() : oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                            .email(email)
                            .password(password)
                            .nickname(nickname)
                            .provider(provider)
                            .providerId(providerId)
                            .build();

                    return userService.createUser(userCreateRequest.toServiceRequest());
                });
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
