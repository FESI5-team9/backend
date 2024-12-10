package com.fesi.mukitlist.api.service.gathering.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fesi.mukitlist.api.service.auth.response.UserResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

public record GatheringWithParticipantsResponse(
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
	Boolean host,
	Boolean favorite,
	List<GatheringParticipantsResponse> participants
) {
	public static GatheringWithParticipantsResponse of(Gathering gathering, User user, List<Keyword> keywords,
		boolean isFavorite, List<GatheringParticipantsResponse> participants) {
		return new GatheringWithParticipantsResponse(
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
			user != null && gathering.getUser().getId().equals(user.getId()),
			isFavorite,
			participants.size() >= gathering.getOpenParticipantCount(),
			participants
		);
	}
}
