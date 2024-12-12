package com.fesi.mukitlist.domain.service.gathering.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fesi.mukitlist.domain.service.auth.response.UserResponse;
import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.Keyword;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

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
	LocalDateTime createdAt,
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
			gathering.getCreatedAt(),
			gathering.getCanceledAt(),
			gathering.isHostUser(user));
	}
}
