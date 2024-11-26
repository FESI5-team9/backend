package com.fesi.mukitlist;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  // 실제 서버 환경에서 테스트
class MukitlistApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;  // HTTP 요청을 보내기 위한 템플릿

	@Test
	void testRegisterUser() {
		// given
		UserCreateRequest userCreateRequest = UserCreateRequest.builder()
			.email("test@test.com")
			.password("password123")
			.nickname("테스트")
			.name("김테스트")
			.build();

		// 응답 상태가 200 OK 인지 확인
		// when
		ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
			"/auth/signin",
			userCreateRequest,
			AuthenticationResponse.class
		);

		// 토큰이 존재하는지 확인
		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().token(), "Token should not be null");
	}
}