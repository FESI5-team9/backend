package com.fesi.mukitlist.api.controller.gathering.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fesi.mukitlist.domain.service.gathering.request.GatheringServiceRequest;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

import lombok.Builder;

/**
 * DTO for {@link Gathering}
 */
public record GatheringRequest(
	List<Long> id,
	GatheringType type,
	LocalDate startDate,
	LocalDate endDate,
	LocationType location,
	String createdBy
) implements Serializable {

	public static GatheringRequest of(List<Long> id, GatheringType type, LocalDate startDate, LocalDate endDate,
		LocationType location,
		String createdBy) {
		return new GatheringRequest(id, type, startDate, endDate, location, createdBy);
	}

	@Builder
	public GatheringServiceRequest toServiceRequest() {
		return GatheringServiceRequest.builder()
			.id(id)
			.type(type)
			.startDate(startDate)
			.endDate(endDate)
			.location(location)
			.createdBy(createdBy)
			.build();
	}
}