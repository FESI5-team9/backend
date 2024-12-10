package com.fesi.mukitlist.api.controller.mypage.response;

import java.util.List;

public record MyPageReviewResponse(
	List<ReviewCompletedList> reviewCompletedList,
	List<ReviewUnCompletedList> reviewUnCompletedList
) {
	public static MyPageReviewResponse of(
		List<ReviewCompletedList> reviewCompletedList,
		List<ReviewUnCompletedList> reviewUnCompletedList) {
		return new MyPageReviewResponse(reviewCompletedList, reviewUnCompletedList);
	}
}
