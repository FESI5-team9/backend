package com.fesi.mukitlist.api.service.gathering.response.v2.composition;

import java.util.List;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;

public record GatheringCreateResponse(
	GatheringBaseResponse gatheringBaseResponse,
	List<String> keywords

) {
	public static GatheringCreateResponse of(Gathering gathering, List<Keyword> keywords) {
		return new GatheringCreateResponse(
			GatheringBaseResponse.of(gathering),
			keywords.stream().map(Keyword::toString).toList()
		);
	}
}
