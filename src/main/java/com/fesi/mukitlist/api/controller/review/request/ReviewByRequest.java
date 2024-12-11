package com.fesi.mukitlist.api.controller.review.request;

import com.fesi.mukitlist.domain.service.review.request.ReviewServiceRequest;

import lombok.Builder;

public record ReviewByRequest(
	Long gatheringId,
	int size,
	int page,
	String sort,
	String direction
) {

	public static ReviewByRequest of(Long gatheringId, int size, int page, String sort, String direction) {
		return new ReviewByRequest(gatheringId, size, page, sort, direction);
	}

	@Builder
	public ReviewServiceRequest toServiceRequest() {
		return ReviewServiceRequest.builder()
			.gatheringId(gatheringId)
			.size(size)
			.page(page)
			.sort(sort)
			.direction(direction)
			.build();
	}
}
