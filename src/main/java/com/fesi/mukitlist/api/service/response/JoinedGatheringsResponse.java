package com.fesi.mukitlist.api.service.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.GatheringType;
import com.fesi.mukitlist.api.domain.UserGathering;

import lombok.Builder;

@Builder
public record JoinedGatheringsResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	String location,
	int participantCount,
	int capacity,
	String createdBy,
	LocalDateTime canceledAt,
	LocalDateTime joinedAt,
	boolean isCompleted,
	boolean isReviewed
) {
	public static JoinedGatheringsResponse of(UserGathering userGathering) {
		return JoinedGatheringsResponse.builder()
			.id(userGathering.getId().getGathering().getId())
			.type(userGathering.getId().getGathering().getType())
			.name(userGathering.getId().getGathering().getName())
			.dateTime(userGathering.getId().getGathering().getDateTime())
			.registrationEnd(userGathering.getId().getGathering().getRegistrationEnd())
			.location(userGathering.getId().getGathering().getLocation())
			.participantCount(userGathering.getId().getGathering().getParticipantCount())
			.capacity(userGathering.getId().getGathering().getCapacity())
			.createdBy(userGathering.getId().getGathering().getCreatedBy())
			.canceledAt(userGathering.getId().getGathering().getCanceledAt())
			.joinedAt(userGathering.getJoinedAt())
			.isCompleted(true)
			.isReviewed(true)
			.build();

	}
}
