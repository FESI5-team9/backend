package com.fesi.mukitlist.api.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserGatheringId {
    private Long userId;
    private Long gatheringId;

    private UserGatheringId(Long userId, Long gatheringId) {
        this.userId = userId;
        this.gatheringId = gatheringId;
    }

    public static UserGatheringId of(Long userId, Long gatheringId) {
        return new UserGatheringId(userId, gatheringId);
    }
}
