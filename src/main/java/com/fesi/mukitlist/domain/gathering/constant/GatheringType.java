package com.fesi.mukitlist.domain.gathering.constant;

public enum GatheringType {
	CAFE("카페"),
	RESTAURANT("식당"),
	PUB("주점"),
	VEGAN("비건");

	private final String type;

	GatheringType(String type) {
		this.type = type;
	}
}
