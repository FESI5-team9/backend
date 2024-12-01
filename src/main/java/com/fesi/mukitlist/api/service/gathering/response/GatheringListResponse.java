package com.fesi.mukitlist.api.service.gathering.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.GatheringType;

public record GatheringListResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	String location,
	String address1,
	int participantCount,
	int capacity,
	String image,
	String createdBy,
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
			gathering.getParticipantCount(),
			gathering.getCapacity(),
			gathering.getImage(),
			gathering.getCreatedBy(),
			gathering.getCanceledAt()
		);
	}
}
