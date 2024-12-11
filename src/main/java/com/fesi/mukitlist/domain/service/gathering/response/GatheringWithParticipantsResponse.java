package com.fesi.mukitlist.domain.service.gathering.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fesi.mukitlist.domain.service.auth.response.UserResponse;
import com.fesi.mukitlist.core.auth.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.Keyword;
import com.fesi.mukitlist.core.gathering.constant.GatheringStatus;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

public record GatheringWithParticipantsResponse(
	Long id,
	UserResponse user,
	GatheringStatus status,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	LocationType location,
	String address1,
	String address2,
	String description,
	List<String> keyword,
	int openParticipantCount,
	int participantCount,
	int capacity,
	String image,
	LocalDateTime createdAt,
	LocalDateTime canceledAt,
	Boolean host,
	Boolean favorite,
	Boolean open,
	List<GatheringParticipantsResponse> participants
) {
	public static GatheringWithParticipantsResponse of(Gathering gathering, User user, List<Keyword> keywords,
		boolean isFavorite, List<GatheringParticipantsResponse> participants) {
		return new GatheringWithParticipantsResponse(
			gathering.getId(),
			UserResponse.of(gathering.getUser()),
			gathering.getStatus(),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getRegistrationEnd(),
			gathering.getLocation(),
			gathering.getAddress1(),
			gathering.getAddress2(),
			gathering.getDescription(),
			keywords.stream().map(Keyword::toString).collect(Collectors.toList()),
			gathering.getOpenParticipantCount(),
			gathering.getParticipantCount(),
			gathering.getCapacity(),
			gathering.getUser().getImage(),
			gathering.getCreatedAt(),
			gathering.getCanceledAt(),
			user != null && gathering.isHostUser(user),
			isFavorite,
			gathering.isOpenedGathering(),
			participants
		);
	}
}
