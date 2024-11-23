package com.fesi.mukitlist.api.service.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fesi.mukitlist.api.domain.GatheringType;

import lombok.Builder;

@Builder
public record GatheringServiceRequest(
	List<Long> id,
	GatheringType type,
	LocalDateTime dateTime,
	String location,
	String createdBy
) {

}
