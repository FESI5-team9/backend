package com.fesi.mukitlist.api.service.gathering.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

public record GatheringUpdateResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	LocationType location,
	String address1,
	String address2,
	List<String> keywords,
	Integer participantCount,
	Integer capacity,
	String image,
	LocalDateTime createdAt
) {
	public static GatheringUpdateResponse of(Gathering gathering, List<Keyword> keywords) {
		return new GatheringUpdateResponse(
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
