package com.fesi.mukitlist.domain.service.gathering.response.v2.composition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.Keyword;

public record GatheringCreateResponse(
	@JsonUnwrapped GatheringBaseResponse gatheringBaseResponse,
	List<String> keywords

) {
	public static GatheringCreateResponse of(Gathering gathering, List<Keyword> keywords) {
		return new GatheringCreateResponse(
			GatheringBaseResponse.of(gathering),
			keywords.stream().map(Keyword::toString).toList()
		);
	}
}
