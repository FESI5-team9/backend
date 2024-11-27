package com.fesi.mukitlist;

import com.fesi.mukitlist.api.controller.auth.request.UserCreateRequest;
import com.fesi.mukitlist.api.controller.auth.response.AuthenticationResponse;
import com.fesi.mukitlist.api.controller.auth.response.UserCreateResponse;
import com.fesi.mukitlist.api.service.auth.request.AuthenticationServiceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

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

		// when
		ResponseEntity<UserCreateResponse> response = restTemplate.postForEntity(
				"/api/auth/signup",  // 사용자 등록 API 엔드포인트
				userCreateRequest,
				UserCreateResponse.class  // 응답은 사용자 등록을 위한 적절한 응답 객체로 설정
		);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);  // 등록된 이메일 중복 INTERNAL_SERVSER_ERROR
		assertThat(response.getBody()).isNotNull();  // 응답 바디가 null이 아님


	}
	@Test
	void testSigninSuccessful() {
		// given
		AuthenticationServiceRequest signinRequest = AuthenticationServiceRequest.builder()
				.email("test@test.com")    // 유효한 이메일
				.password("password123")   // 유효한 비밀번호
				.build();

		// when
		ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
				"/api/auth/signin",       // 로그인 API 엔드포인트
				signinRequest,            // 로그인 요청 객체
				AuthenticationResponse.class // 응답 클래스
		);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);  // 200 OK 응답 코드 확인
		assertThat(response.getBody()).isNotNull();  // 응답 바디가 null이 아님
		assertThat(response.getBody().token()).isNotBlank();  // JWT 토큰이 null이나 공백이 아닌지 확인
	}
}
