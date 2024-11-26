package com.fesi.mukitlist.api.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {
	NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "모임을 찾을 수 없습니다"),
	MINIMUM_CAPACITY(HttpStatus.BAD_REQUEST, "MINIMUM_CAPACITY", "최소 인원은 5명입니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "모임을 취소할 권한이 없습니다"),
	GATHERING_CANCELED(HttpStatus.BAD_REQUEST, "GATHERING_CANCELED", "취소된 모임입니다."),
	MAXIMUM_PARTICIPANTS(HttpStatus.BAD_REQUEST, "MAXIMUM_PARTICIPANTS", "정원 초과 입니다."),
	PAST_GATHERING(HttpStatus.BAD_REQUEST, "PAST_GATHERING" , "이미 지난 모임입니다"),

	NOT_PARTICIPANTS(HttpStatus.FORBIDDEN, "FORBIDDEN", "모임에 참석하지 않았습니다.");

	private HttpStatus status;
	private String code;
	private String message;

	ExceptionCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
