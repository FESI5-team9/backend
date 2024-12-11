package com.fesi.mukitlist.domain.service.gathering.request;

import java.time.LocalDate;
import java.util.List;

import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

import lombok.Builder;

@Builder
public record GatheringServiceRequest(
	List<Long> id,
	GatheringType type,
	LocalDate startDate,
	LocalDate endDate,
	LocationType location,
	String createdBy,
	int size,
	int page,
	String sort,
	String direction
) {

}
