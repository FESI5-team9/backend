package com.fesi.mukitlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.response.UserCreateResponse;
import com.fesi.mukitlist.api.service.auth.JwtService;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;
import com.fesi.mukitlist.api.service.auth.response.AuthenticationServiceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  // 실제 서버 환경에서 테스트
class MukitlistApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;  // HTTP 요청을 보내기 위한 템플릿

	@Autowired
	private JwtService jwtService;  // JwtService를 자동 주입

	private UserDetails userDetails;

	@Test
	void testRegisterUser() {
		// given
		UserCreateRequest userCreateRequest = UserCreateRequest.builder()
				.email("test@test.com")
				.password("password123")
				.nickname("테스트")
				.name("김테스트")
				.build();

		// when
		ResponseEntity<UserCreateResponse> response = restTemplate.postForEntity(
				"/api/auth/signup",  // 사용자 등록 API 엔드포인트
				userCreateRequest,
				UserCreateResponse.class  // 응답은 사용자 등록을 위한 적절한 응답 객체로 설정
		);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);  // 등록된 이메일 중복 INTERNAL_SERVER_ERROR
		assertThat(response.getBody()).isNotNull();  // 응답 바디가 null이 아님
	}

	@Test
	void testLogin() throws Exception {
		// given: 로그인 요청을 위한 데이터 설정 (email과 password를 body로 보냄)
		AuthenticationServiceRequest loginRequest = AuthenticationServiceRequest.builder()
				.email("test@test.com")
				.password("password123")
				.build();

		// ObjectMapper를 사용하여 loginRequest 객체를 JSON으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writeValueAsString(loginRequest);

		// HTTP 헤더에 Content-Type을 application/json으로 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// HttpEntity에 JSON 형식의 requestBody와 headers를 넣어 요청 생성
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		// when: 로그인 요청을 POST로 보낼 때 body에 JSON 형식으로 requestBody를 넣어 보냄
		ResponseEntity<AuthenticationServiceResponse> response = restTemplate.exchange(
				"/api/auth/signin",  // 로그인 API 엔드포인트
				HttpMethod.POST,
				entity,  // HttpEntity를 사용하여 body와 headers를 함께 보냄
				AuthenticationServiceResponse.class
		);

		// then: 로그인 성공 시 응답 코드 200 OK 확인
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().accessToken()).isNotNull();  // 액세스 토큰이 있어야 함
		assertThat(response.getBody().refreshToken()).isNotNull();  // 리프레시 토큰이 있어야 함

	}
}
