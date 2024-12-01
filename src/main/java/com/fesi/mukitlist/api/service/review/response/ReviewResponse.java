package com.fesi.mukitlist.api.service.review.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.service.gathering.response.GatheringReviewResponse;
import com.fesi.mukitlist.domain.Review;
import com.fesi.mukitlist.api.service.gathering.response.GatheringResponse;
import com.fesi.mukitlist.domain.auth.User;

import lombok.Builder;

@Builder
public record ReviewResponse(
	Long id,
	Long userId,
	Long gatheringId,
	int score,
	String comment,
	LocalDateTime createdAt

)
{
	public static ReviewResponse of(Review review) {
		return ReviewResponse.builder()
			.id(review.getId())
			.userId(review.getUser().getId())
			.gatheringId(review.getGathering().getId())
			.score(review.getScore())
			.comment(review.getComment())
			.createdAt(review.getCreatedAt())
			.build();
	}
}
