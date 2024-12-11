package com.fesi.mukitlist.api.controller.mypage.response;

import com.fesi.mukitlist.domain.service.gathering.response.GatheringListResponse;

public record ReviewUnCompletedList(
	GatheringListResponse gathering
) {
	public static ReviewUnCompletedList of(GatheringListResponse gathering) {
		return new ReviewUnCompletedList(gathering);
	}
}
