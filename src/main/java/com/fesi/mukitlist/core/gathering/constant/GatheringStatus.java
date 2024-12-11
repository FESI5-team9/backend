package com.fesi.mukitlist.core.gathering.constant;

import lombok.Getter;

@Getter
public enum GatheringStatus {
	RECRUITING("모집 중"),
	RECRUITMENT_COMPLETED("모집 완료");

	private final String description;

	GatheringStatus(String description) {
		this.description = description;
	}
}
