package com.fesi.mukitlist.domain.service.gathering.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringStatus;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

public record GatheringListResponse(
	Long id,
	GatheringStatus status,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	LocationType location,
	String address1,
	String address2,
	Boolean open,
	Boolean favorite,
	Boolean participation,
	int participantCount,
	int capacity,
	String image,
	LocalDateTime createdAt,
	LocalDateTime canceledAt
) {
	public static GatheringListResponse of(Gathering gathering, Boolean participation, Boolean isFavorite) {
		return new GatheringListResponse(
			gathering.getId(),
			gathering.getStatus(),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getRegistrationEnd(),
			gathering.getLocation(),
			gathering.getAddress1(),
			gathering.getAddress2(),
			gathering.isOpenedGathering(),
			isFavorite,
			participation,
			gathering.getOpenParticipantCount(),
			gathering.getCapacity(),
			gathering.getImage(),
			gathering.getCreatedAt(),
			gathering.getCanceledAt()
		);
	}

	public static GatheringListResponse of(Gathering gathering) {
		return new GatheringListResponse(
			gathering.getId(),
			gathering.getStatus(),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getRegistrationEnd(),
			gathering.getLocation(),
			gathering.getAddress1(),
			gathering.getAddress2(),
			gathering.isOpenedGathering(),
			null,
			null,
			gathering.getOpenParticipantCount(),
			gathering.getCapacity(),
			gathering.getImage(),
			gathering.getCreatedAt(),
			gathering.getCanceledAt()
		);
	}
}
