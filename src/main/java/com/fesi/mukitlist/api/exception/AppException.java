package com.fesi.mukitlist.api.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
	private final ExceptionCode exceptionCode;

	@Builder
	public AppException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
