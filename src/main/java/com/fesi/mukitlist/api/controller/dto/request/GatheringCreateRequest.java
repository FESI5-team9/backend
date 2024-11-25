package com.fesi.mukitlist.api.controller.dto.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.GatheringType;
import com.fesi.mukitlist.api.service.request.GatheringServiceCreateRequest;

import jakarta.validation.constraints.NotBlank;

public record GatheringCreateRequest(

	GatheringType type,

	@NotBlank(message = "모임 장소를 입력해주세요.")
	String location,

	@NotBlank(message = "모임 이름을 입력해주세요.")
	String name,

	LocalDateTime dateTime,

	int capacity,

	// MultipartFile image
	LocalDateTime registrationEnd
) {

	public GatheringServiceCreateRequest toServiceRequest() {
		return GatheringServiceCreateRequest.builder()
			.type(type)
			.location(location)
			.name(name)
			.dateTime(dateTime)
			.capacity(capacity)
			.registrationEnd(registrationEnd)
			.build();
	}
}
