package com.fesi.mukitlist.core.gathering.favorite;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class FavoriteGatheringId {

	private Long userId;

	private Long gatheringId;

	public FavoriteGatheringId(Long userId, Long gatheringId) {
		this.userId = userId;
		this.gatheringId = gatheringId;
	}

	public static FavoriteGatheringId of(Long userId, Long gatheringId) {
		return new FavoriteGatheringId(userId, gatheringId);
	}
}
