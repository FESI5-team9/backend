package com.fesi.mukitlist.api.controller.gathering.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fesi.mukitlist.domain.gathering.GatheringType;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO for {@link Gathering}
 */
public record GatheringRequest(

	List<Long> id,
	GatheringType type,
	LocalDateTime dateTime,
	String location,
	String createdBy,
	int size,
	int page,
	String sort,
	String direction
)
	implements Serializable {

	public static GatheringRequest of(List<Long> id, GatheringType type, LocalDateTime dateTime, String location,
		String createdBy, int size, int page, String sort, String direction) {
		return new GatheringRequest(id, type, dateTime, location, createdBy, size, page, sort, direction);
	}

	@Builder
	public GatheringServiceRequest toServiceRequest() {
		return GatheringServiceRequest.builder()
			.id(id)
			.type(type)
			.dateTime(dateTime)
			.location(location)
			.createdBy(createdBy)
			.size(size)
			.page(page)
			.sort(sort)
			.direction(direction)
			.build();
	}
}