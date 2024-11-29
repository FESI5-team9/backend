package com.fesi.mukitlist.api.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Validation Error Response")
@Getter
public class ValidationErrorResponse {
	@Schema(description = "오류 코드", example = "VALIDATION_ERROR")
	private String code;

	@Schema(description = "유효성 검사 실패한 파라미터", example = "type")
	private String parameter;

	@Schema(description = "오류 메시지", example = "유효한 모임 종류를 입력하세요")
	private String message;

	private ValidationErrorResponse(String code, String parameter, String message) {
		this.code = code;
		this.parameter = parameter;
		this.message = message;
	}

	public static ValidationErrorResponse of(String code, String parameter, String message) {
		return new ValidationErrorResponse(code, parameter, message);
	}
}
