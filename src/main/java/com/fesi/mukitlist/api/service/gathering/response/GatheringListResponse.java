package com.fesi.mukitlist.api.service.gathering.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

public record GatheringListResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	LocationType location,
	String address1,
	String address2,
	int participantCount,
	int capacity,
	String image,
	LocalDateTime createdAt,
	LocalDateTime canceledAt
) {
	public static GatheringListResponse of(Gathering gathering) {
		return new GatheringListResponse(
			gathering.getId(),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getRegistrationEnd(),
			gathering.getLocation(),
			gathering.getAddress1(),
			gathering.getAddress2(),
			gathering.getParticipantCount(),
			gathering.getCapacity(),
			gathering.getImage(),
			gathering.getCreatedAt(),
			gathering.getCanceledAt()
		);
	}
}
