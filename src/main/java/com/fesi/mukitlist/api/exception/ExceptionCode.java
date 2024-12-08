package com.fesi.mukitlist.api.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {
	//유저
	EMAIL_EXIST(HttpStatus.BAD_REQUEST, "EMAIL_EXIST", "중복된 이메일입니다"),
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT_FOUND", "사용자를 찾을 수 없습니다."),
	LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "LOGIN_REQUIRED", "로그인이 필요합니다."),
	//인증
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "토큰이 만료되었습니다."),
	NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "NOT_VALID_TOKEN", "해당 토큰은 유효한 토큰이 아닙니다."),
	//모임
	AT_LEAST_ONE(HttpStatus.BAD_REQUEST, "AT_LEAST_ONE", "size는 최소 1이어야 합니다"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "모임을 찾을 수 없습니다"),
	MINIMUM_CAPACITY(HttpStatus.BAD_REQUEST, "MINIMUM_CAPACITY", "최소 인원은 5명입니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "모임을 취소할 권한이 없습니다"),
	GATHERING_CANCELED(HttpStatus.BAD_REQUEST, "GATHERING_CANCELED", "취소된 모임입니다."),
	MAXIMUM_PARTICIPANTS(HttpStatus.BAD_REQUEST, "MAXIMUM_PARTICIPANTS", "정원 초과 입니다."),
	PAST_GATHERING(HttpStatus.BAD_REQUEST, "PAST_GATHERING", "이미 지난 모임입니다"),
	NOT_PARTICIPANTS(HttpStatus.FORBIDDEN, "FORBIDDEN", "모임에 참석하지 않았습니다."),
	//파일
	RESOURCE_SIZE_LIMIT(HttpStatus.BAD_REQUEST, "SIZE_LIMIT", "파일의 용량은 10mb 이하여야 합니다.");

	private HttpStatus status;
	private String code;
	private String message;

	ExceptionCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
