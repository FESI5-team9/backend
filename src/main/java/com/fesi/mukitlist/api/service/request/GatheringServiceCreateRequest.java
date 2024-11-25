package com.fesi.mukitlist.api.service.request;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.GatheringType;
import com.fesi.mukitlist.api.exception.AppException;

import lombok.Builder;

@Builder
public record GatheringServiceCreateRequest(
	GatheringType type,
	String location,
	String name,
	LocalDateTime dateTime,
	int capacity,

	// MultipartFile image
	LocalDateTime registrationEnd
) {
	public int minimumCapacity() {
		if (capacity < 5) throw new AppException(MINIMUM_CAPACITY);
		return capacity;
	}
}
