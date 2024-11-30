package com.fesi.mukitlist.api.repository.user;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.response.UserCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegistrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private String email;
    private String password;
    private String nickname;
    private String name;

    @BeforeEach
    void setUp() {
        email = "user4@example.com";
        password = "password4";
        nickname = "Company D";
        name = "test4";
    }

    @Test
    void testRegisterUser() {
        // given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .name(name)
                .build();

        // when
        ResponseEntity<UserCreateResponse> response = restTemplate.postForEntity(
                "/api/auth/signup",
                userCreateRequest,
                UserCreateResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }
}