package com.fesi.mukitlist.api.service.review.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.service.auth.response.UserResponse;
import com.fesi.mukitlist.api.service.gathering.response.GatheringReviewResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.Review;

import lombok.Builder;

@Builder
public record ReviewWithGatheringAndUserResponse(
	Long id,
	int score,
	String comment,
	LocalDateTime createdAt,
	GatheringReviewResponse gathering,
	UserResponse user
	)
{
	public static ReviewWithGatheringAndUserResponse of(Review review) {
		return ReviewWithGatheringAndUserResponse.builder()
			.id(review.getId())
			.score(review.getScore())
			.comment(review.getComment())
			.createdAt(review.getCreatedAt())
			.user(UserResponse.of(review.getUser()))
			.gathering(GatheringReviewResponse.of(review.getGathering()))
			.build();
	}
}
