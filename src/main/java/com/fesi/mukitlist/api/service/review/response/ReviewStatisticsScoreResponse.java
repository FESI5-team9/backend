package com.fesi.mukitlist.api.service.review.response;

import java.util.Map;

import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;

import lombok.Builder;

@Builder
public record ReviewStatisticsScoreResponse(
	double averageScore,
	Long oneStar,
	Long twoStars,
	Long threeStars,
	Long fourStars,
	Long fiveStars
) {
	public static ReviewStatisticsScoreResponse of(double averageScore, Map<Integer, Long> score) {
		return ReviewStatisticsScoreResponse.builder()
			.averageScore(averageScore)
			.oneStar(score.getOrDefault(1,0L))
			.twoStars(score.getOrDefault(2,0L))
			.threeStars(score.getOrDefault(3,0L))
			.fourStars(score.getOrDefault(4,0L))
			.fiveStars(score.getOrDefault(5,0L))
			.build();
	}
}
