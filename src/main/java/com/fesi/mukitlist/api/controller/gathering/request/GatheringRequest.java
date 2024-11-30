package com.fesi.mukitlist.api.controller.gathering.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceRequest;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.GatheringType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO for {@link Gathering}
 */
public record GatheringRequest(
	List<Long> id,
	GatheringType type,
	LocalDate dateTime,
	String location,
	String createdBy,
	@Schema(description = "페이지 크기", defaultValue = "10") int size,
	@Schema(description = "페이지 번호", defaultValue = "0") int page,
	@Schema(description = "정렬 기준", defaultValue = "dateTime") String sort,
	@Schema(description = "정렬 방향", defaultValue = "ASC") String direction
) implements Serializable {

	public GatheringRequest {
		size = (size == 0) ? 10 : size;  // 기본값 설정
		sort = (sort == null) ? "dateTime" : sort;  // 기본값 설정
		direction = (direction == null) ? "ASC" : direction;  // 기본값 설정
	}

	public static GatheringRequest of(List<Long> id, GatheringType type, LocalDate dateTime, String location,
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