package com.fesi.mukitlist.domain.service.review.request;

import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.Review;

import lombok.Builder;

@Builder
public record ReviewServiceCreateRequest(
	Long gatheringId,
	int score,
	String comment
) {
	public ReviewServiceCreateRequest of(Review review) {
		return ReviewServiceCreateRequest.builder()
			.gatheringId(review.getGathering().getId())
			.score(review.getScore())
			.comment(review.getComment())
			.build();
	}

	public Review toEntity(Gathering gathering, User user) {
		return Review.of(
			score,
			comment,
			gathering,
			user
		);
	}
}