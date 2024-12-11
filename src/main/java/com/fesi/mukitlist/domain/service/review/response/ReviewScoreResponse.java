package com.fesi.mukitlist.domain.service.review.response;

import java.util.Map;

import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;

import lombok.Builder;

@Builder
public record ReviewScoreResponse(
	Long gatheringId,
	GatheringType type,
	double averageScore,
	Long oneStar,
	Long twoStars,
	Long threeStars,
	Long fourStars,
	Long fiveStars
) {
	public static ReviewScoreResponse of(Gathering gathering, double averageScore, Map<Integer, Long> score) {
		return ReviewScoreResponse.builder()
			.gatheringId(gathering.getId())
			.type(gathering.getType())
			.averageScore(averageScore)
			.oneStar(score.getOrDefault(1,0L))
			.twoStars(score.getOrDefault(2,0L))
			.threeStars(score.getOrDefault(3,0L))
			.fourStars(score.getOrDefault(4,0L))
			.fiveStars(score.getOrDefault(5,0L))
			.build();
	}
}
