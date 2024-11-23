package com.fesi.mukitlist.api.controller.dto.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.Gathering;
import com.fesi.mukitlist.api.domain.GatheringType;

import lombok.Builder;

@Builder
public record GatheringResponse(
	Long id,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	LocalDateTime registrationEnd,
	String location,
	int participantCount,
	int capacity
) {

	public static GatheringResponse of(Gathering gathering) {
		return GatheringResponse.builder()
			.id(gathering.getId())
			.type(gathering.getType())
			.name(gathering.getName())
			.dateTime(gathering.getDateTime())
			.registrationEnd(gathering.getRegistrationEnd())
			.location(gathering.getLocation())
			.participantCount(gathering.getParticipantCount())
			.capacity(gathering.getCapacity())
			.build();
	}
}
