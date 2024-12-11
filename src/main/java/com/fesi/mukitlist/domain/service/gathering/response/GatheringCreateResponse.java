package com.fesi.mukitlist.domain.service.gathering.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.Keyword;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

public record GatheringCreateResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	LocationType location,
	String address1,
	String address2,
	List<String> keywords,
	int participantCount,
	int capacity,
	String image,
	LocalDateTime createdAt
) {
	public static GatheringCreateResponse of(Gathering gathering, List<Keyword> keywords) {
		return new GatheringCreateResponse(
			gathering.getId(),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getRegistrationEnd(),
			gathering.getLocation(),
			gathering.getAddress1(),
			gathering.getAddress2(),
			keywords.stream().map(Keyword::toString).toList(),
			gathering.getParticipantCount(),
			gathering.getCapacity(),
			gathering.getImage(),
			gathering.getCreatedAt()
		);
	}
}
