package com.fesi.mukitlist.api.service.gathering.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.GatheringType;
public record GatheringReviewResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	String location,
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
