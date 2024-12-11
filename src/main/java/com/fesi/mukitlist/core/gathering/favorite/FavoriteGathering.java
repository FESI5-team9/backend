package com.fesi.mukitlist.core.gathering.favorite;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FavoriteGathering {

	@EmbeddedId
	private FavoriteGatheringId id;

	private FavoriteGathering(FavoriteGatheringId id) {
		this.id = id;
	}

	public static FavoriteGathering of (FavoriteGatheringId id) {
		return new FavoriteGathering(id);
	}
}
