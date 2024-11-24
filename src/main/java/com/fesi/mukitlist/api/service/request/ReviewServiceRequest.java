package com.fesi.mukitlist.api.service.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.GatheringType;

import lombok.Builder;

@Builder
public record ReviewServiceRequest(
	Long gatheringId,
	Long userId,
	GatheringType type,
	String location,
	LocalDateTime date,
	LocalDateTime registrationEnd
) {

}
