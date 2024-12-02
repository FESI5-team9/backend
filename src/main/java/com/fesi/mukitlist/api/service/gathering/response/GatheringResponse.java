package com.fesi.mukitlist.api.service.gathering.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fesi.mukitlist.api.service.auth.response.UserResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.GatheringType;
import com.fesi.mukitlist.domain.gathering.Keyword;
import com.fesi.mukitlist.domain.gathering.LocationType;

public record GatheringResponse(
	Long id,
	UserResponse user,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	LocationType location,
	String address1,
	String address2,
	String description,
	List<String> keyword,
	int participantCount,
	int capacity,
	String image,
	String createdBy,
	LocalDateTime canceledAt,
	Boolean host
) {
	public static GatheringResponse of(Gathering gathering, User user, List<Keyword> keywords) {
		return new GatheringResponse(
			gathering.getId(),
			UserResponse.of(gathering.getUser()),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getRegistrationEnd(),
			gathering.getLocation(),
			gathering.getAddress1(),
			gathering.getAddress2(),
			gathering.getDescription(),
			keywords.stream().map(Keyword::toString).collect(Collectors.toList()),
			gathering.getParticipantCount(),
			gathering.getCapacity(),
			gathering.getUser().getImage(),
			gathering.getUser().getName(),
			gathering.getCanceledAt(),
			gathering.isHostUser(user)
		);
	}
}
