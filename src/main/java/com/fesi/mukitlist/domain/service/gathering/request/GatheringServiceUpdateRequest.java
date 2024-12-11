package com.fesi.mukitlist.domain.service.gathering.request;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

import lombok.Builder;

@Builder
public record GatheringServiceUpdateRequest(
	LocationType location,
	GatheringType type,
	String name,
	LocalDateTime dateTime,
	Integer openParticipantCount,
	Integer capacity,
	MultipartFile image,
	String address1,
	String address2,
	String description,
	List<String> keyword
) {

	public int minimumCapacity() {
		if (capacity < 5) throw new AppException(MINIMUM_CAPACITY);
		return capacity;
	}
}
