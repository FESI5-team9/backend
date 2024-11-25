package com.fesi.mukitlist.api.controller.dto.request;

import java.time.LocalDateTime;
import java.util.List;

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
			.registrationEnd(registrationEnd)
			.address1(address1)
			.address2(address2)
			.description(description)
			.keyword(keyword)
			.build();
	}
}
