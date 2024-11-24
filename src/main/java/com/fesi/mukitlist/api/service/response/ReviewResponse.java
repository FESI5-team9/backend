package com.fesi.mukitlist.api.service.response;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.Review;

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
