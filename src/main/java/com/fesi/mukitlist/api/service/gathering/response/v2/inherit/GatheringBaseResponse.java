package com.fesi.mukitlist.api.service.gathering.response.v2.inherit;

import java.time.LocalDateTime;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GatheringBaseResponse {
	private Long id;
	private GatheringType type;
	private String name;
	private LocalDateTime dateTime;
	private LocalDateTime registrationEnd;
	private LocationType location;
	private String address1;
	private String address2;
	private int participantCount;
	private int capacity;
	private String image;
	private String createdBy;

	@Builder
	protected GatheringBaseResponse(
		Long id,
		GatheringType type,
		String name,
		LocalDateTime dateTime,
		LocalDateTime registrationEnd,
		LocationType location,
		String address1,
		String address2,
		int participantCount,
		int capacity,
		String image,
		String createdBy
	) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.dateTime = dateTime;
		this.registrationEnd = registrationEnd;
		this.location = location;
		this.address1 = address1;
		this.address2 = address2;
		this.participantCount = participantCount;
		this.capacity = capacity;
		this.image = image;
		this.createdBy = createdBy;
	}

	public static GatheringBaseResponse of(Gathering gathering) {
		return new GatheringBaseResponse(
			gathering.getId(),
			gathering.getType(),
			gathering.getName(),
			gathering.getDateTime(),
			gathering.getRegistrationEnd(),
			gathering.getLocation(),
			gathering.getAddress1(),
			gathering.getAddress2(),
			gathering.getParticipantCount(),
			gathering.getCapacity(),
			gathering.getUser().getImage(),
			gathering.getUser().getName());
	}
}
