package com.fesi.mukitlist.api.controller.review.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.api.service.review.request.ReviewServiceRequest;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

import lombok.Builder;

public record ReviewRequest(
	Long gatheringId,
	Long userId,
	GatheringType type,
	LocationType location,
	LocalDateTime registrationEnd,
	int size,
	int page,
	String sort,
	String direction
) {

	public static ReviewRequest of(Long gatheringId, Long userId, GatheringType type, LocationType location,
		LocalDateTime registrationEnd, int size, int page, String sort, String direction) {
		return new ReviewRequest(gatheringId, userId, type, location, registrationEnd, size, page, sort, direction);
	}

	@Builder
	public ReviewServiceRequest toServiceRequest() {
		return ReviewServiceRequest.builder()
			.gatheringId(gatheringId)
			.userId(userId)
			.type(type)
			.location(location)
			.registrationEnd(registrationEnd)
			.size(size)
			.page(page)
			.sort(sort)
			.direction(direction)
			.build();
	}
}
