package com.fesi.mukitlist.api.controller.dto.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.GatheringType;
import com.fesi.mukitlist.api.service.request.GatheringServiceCreateRequest;

public record GatheringCreateRequest(
	GatheringType type,
	String location,
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
