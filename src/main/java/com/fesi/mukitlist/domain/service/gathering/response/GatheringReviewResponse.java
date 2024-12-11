package com.fesi.mukitlist.domain.service.gathering.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

public record GatheringReviewResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocationType location,
	String image
) {
	public static GatheringReviewResponse of(Gathering gathering) {
		return new GatheringReviewResponse(
			gathering.getId(),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getLocation(),
			gathering.getImage()
		);
	}
}
