package com.fesi.mukitlist;

import com.fesi.mukitlist.api.controller.dto.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.dto.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  // 실제 서버 환경에서 테스트
class MukitlistApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;  // HTTP 요청을 보내기 위한 템플릿

	private UserCreateRequest userCreateRequest;

	@BeforeEach
	void setUp() {
		// 테스트용 사용자 데이터 설정
		userCreateRequest = new UserCreateRequest(
				"test" + System.currentTimeMillis() + "@example.com",
				"password123",
				"테스트 사용자",
				"테스트 회사",
				null
		);
	}

	@Test
	void testRegisterUser() {
		// 실제 POST 요청을 보냄
		ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
				"/auth/signin",
				userCreateRequest,
				AuthenticationResponse.class
		);

		// 응답 상태가 200 OK 인지 확인
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// 토큰이 존재하는지 확인
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().token(), "Token should not be null");

	}
}