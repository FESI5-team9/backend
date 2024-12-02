package com.fesi.mukitlist.api.controller.gathering.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceCreateRequest;
import com.fesi.mukitlist.domain.gathering.GatheringType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GatheringCreateRequest(
	@Schema(description = "모임 서비스 종류", example = "RESTAURANT")
	GatheringType type,

	@Schema(description = "모임 장소", example = "서울 강남구")
	@NotBlank(message = "모임 장소를 입력해주세요.")
	String location,

	@Schema(description = "모임 이름", example = "맛집 탐방")
	@NotBlank(message = "모임 이름을 입력해주세요.")
	String name,

	@Schema(description = "모임 날짜 및 시간('YYYY-MM-DDTHH:MM:SS')", example = "2024-12-17T23:43:54", format = "date-time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime dateTime,

	@Schema(description = "모집 정원 (최소 5인 이상)", example = "10", minimum = "5")
	int capacity,

	@Schema(description = "모임 이미지", type = "string", format = "binary")
	MultipartFile image,

	@Schema(description = "모임 등록 마감일", example = "2024-12-16T23:59:59", format = "date-time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime registrationEnd,

	String address1,
	String address2,
	String description,
	List<String> keyword
) {

	public GatheringServiceCreateRequest toServiceRequest() {
		return GatheringServiceCreateRequest.builder()
			.location(location)
			.type(type)
			.name(name)
			.dateTime(dateTime)
			.capacity(capacity)
			.image(image)
			.registrationEnd(registrationEnd)
			.address1(address1)
			.address2(address2)
			.description(description)
			.keyword(keyword)
			.build();
	}
}
