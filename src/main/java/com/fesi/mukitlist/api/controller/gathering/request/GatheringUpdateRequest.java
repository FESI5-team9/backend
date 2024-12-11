package com.fesi.mukitlist.api.controller.gathering.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fesi.mukitlist.domain.service.gathering.request.GatheringServiceUpdateRequest;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

import io.swagger.v3.oas.annotations.media.Schema;

public record GatheringUpdateRequest(
	@Schema(description = "모임 서비스 종류", example = "RESTAURANT")
	GatheringType type,

	@Schema(description = "모임 장소", example = "SEOUL")
	LocationType location,

	@Schema(description = "식당 이름", example = "런던 베이글")
	String name,

	@Schema(description = "모임 날짜 및 시간('YYYY-MM-DDTHH:MM:SS')", example = "2024-12-17T23:43:54", format = "date-time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime dateTime,

	@Schema(description = "최소 모집 인원 (자동 개설 확정)", example = "3")
	Integer openParticipantCount,

	@Schema(description = "모집 정원 (최소 5인 이상)", example = "10", minimum = "5")
	Integer capacity,

	@Schema(description = "모임 이미지", type = "string", format = "binary")
	MultipartFile image,

	String address1,
	String address2,
	String description,
	List<String> keyword
) {

	public GatheringServiceUpdateRequest toServiceRequest() {
		return new GatheringServiceUpdateRequest(
			location,
			type,
			name,
			dateTime,
			openParticipantCount,
			capacity,
			image,
			address1,
			address2,
			description,
			keyword
		);
	}
}
