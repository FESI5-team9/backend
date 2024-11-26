package com.fesi.mukitlist.api.controller.dto.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.GatheringType;
import com.fesi.mukitlist.api.service.request.ReviewServiceRequest;

import lombok.Builder;

public record ReviewRequest(
	Long gatheringId,
	Long userId,
	GatheringType type,
	String location,
	LocalDateTime date,
	LocalDateTime registrationEnd

) {

	@Builder
	public ReviewServiceRequest toServiceRequest() {
		return ReviewServiceRequest.builder()
			.gatheringId(gatheringId)
			.userId(userId)
			.type(type)
			.location(location)
			.date(date)
			.registrationEnd(registrationEnd)
			.build();
	}

	public static ReviewRequest of(Long gatheringId, Long userId, GatheringType type, String location,
		LocalDateTime date, LocalDateTime registrationEnd) {
		return new ReviewRequest(gatheringId, userId, type, location, date, registrationEnd);
	}
}
