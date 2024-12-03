package com.fesi.mukitlist.api.service.review.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

import lombok.Builder;

@Builder
public record ReviewServiceRequest(
	Long gatheringId,
	Long userId,
	GatheringType type,
	LocationType location,
	LocalDateTime date,
	LocalDateTime registrationEnd,
	int size,
	int page,
	String sort,
	String direction
) {

}
