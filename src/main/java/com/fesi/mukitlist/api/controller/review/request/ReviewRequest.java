package com.fesi.mukitlist.api.controller.review.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.gathering.GatheringType;
import com.fesi.mukitlist.api.service.review.request.ReviewServiceRequest;

import lombok.Builder;

public record ReviewRequest(
	Long gatheringId,
	Long userId,
	GatheringType type,
	String location,
	LocalDateTime date,
	LocalDateTime registrationEnd,
	int size,
	int page,
	String sort,
	String direction
) {

	public static ReviewRequest of(Long gatheringId, Long userId, GatheringType type, String location,
		LocalDateTime date, LocalDateTime registrationEnd, int size, int page, String sort, String direction) {
		return new ReviewRequest(gatheringId, userId, type, location, date, registrationEnd, size, page, sort, direction);
	}

	@Builder
	public ReviewServiceRequest toServiceRequest() {
		return ReviewServiceRequest.builder()
			.gatheringId(gatheringId)
			.userId(userId)
			.type(type)
			.location(location)
			.date(date)
			.registrationEnd(registrationEnd)
			.size(size)
			.page(page)
			.sort(sort)
			.direction(direction)
			.build();
	}
}
