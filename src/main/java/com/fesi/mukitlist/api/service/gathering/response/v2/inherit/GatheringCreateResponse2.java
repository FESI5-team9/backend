package com.fesi.mukitlist.api.service.gathering.response.v2.inherit;

import java.util.List;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;

import lombok.Getter;

@Getter
public class GatheringCreateResponse2 extends GatheringBaseResponse {

	private final List<String> keywords;

	public GatheringCreateResponse2(Gathering gathering, List<String> keywords) {
		super(
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
			gathering.getImage(),
			gathering.getCreatedBy()
			);
		this.keywords = keywords;
	}

	public static GatheringCreateResponse2 of(Gathering gathering, List<Keyword> keywords) {
		return new GatheringCreateResponse2(gathering, keywords.stream().map(Keyword::toString).toList());
	}
}
