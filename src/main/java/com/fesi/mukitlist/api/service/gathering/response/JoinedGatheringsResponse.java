package com.fesi.mukitlist.api.service.gathering.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.Keyword;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;
import com.fesi.mukitlist.domain.usergathering.UserGathering;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record JoinedGatheringsResponse(
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
	String createdBy,
	LocalDateTime canceledAt,
	LocalDateTime joinedAt,
	Boolean isCompleted,
	Boolean isReviewed
) {
	public static JoinedGatheringsResponse of(UserGathering userGathering, Boolean isCompleted, Boolean isReviewed,
		List<Keyword> keywords) {
		return JoinedGatheringsResponse.builder()
			.id(userGathering.getId().getGathering().getId())
			.type(userGathering.getId().getGathering().getType())
			.name(userGathering.getId().getGathering().getName())
			.dateTime(userGathering.getId().getGathering().getDateTime())
			.registrationEnd(userGathering.getId().getGathering().getRegistrationEnd())
			.location(userGathering.getId().getGathering().getLocation())
			.address1(userGathering.getId().getGathering().getAddress1())
			.address2(userGathering.getId().getGathering().getAddress2())
			.keywords(keywords.stream().map(Keyword::toString).collect(Collectors.toList()))
			.participantCount(userGathering.getId().getGathering().getParticipantCount())
			.capacity(userGathering.getId().getGathering().getCapacity())
			.image(userGathering.getId().getGathering().getImage())
			.createdBy(userGathering.getId().getGathering().getCreatedBy())
			.canceledAt(userGathering.getId().getGathering().getCanceledAt())
			.joinedAt(userGathering.getJoinedAt())
			.isCompleted(isCompleted)
			.isReviewed(isReviewed)
			.build();
	}
}
