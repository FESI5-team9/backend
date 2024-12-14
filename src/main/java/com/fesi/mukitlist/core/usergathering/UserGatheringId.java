package com.fesi.mukitlist.core.usergathering;

import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.gathering.Gathering;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserGatheringId {
	@ManyToOne
	private User user;
	@ManyToOne
	private Gathering gathering;

	private UserGatheringId(User user, Gathering gathering) {
		this.user = user;
		this.gathering = gathering;
	}

	public static UserGatheringId of(User user, Gathering gathering) {
		return new UserGatheringId(user, gathering);
	}
}
