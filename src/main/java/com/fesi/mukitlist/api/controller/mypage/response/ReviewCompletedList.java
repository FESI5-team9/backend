package com.fesi.mukitlist.api.controller.mypage.response;

import com.fesi.mukitlist.api.service.gathering.response.GatheringListResponse;
import com.fesi.mukitlist.api.service.review.response.ReviewResponse;

public record ReviewCompletedList(
	GatheringListResponse gathering,
	ReviewResponse review
) {
	public static ReviewCompletedList of(GatheringListResponse gathering, ReviewResponse review) {
		return new ReviewCompletedList(gathering, review);
	}
}
