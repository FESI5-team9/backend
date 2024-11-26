package com.fesi.mukitlist.api.repository.usergathering;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.fesi.mukitlist.domain.Review;
import com.fesi.mukitlist.domain.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.usergathering.UserGathering;
import com.fesi.mukitlist.domain.usergathering.UserGatheringId;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class UserGatheringSpecifications {

	public static Specification<UserGathering> byUser(User user) {
		return (root, query, criteriaBuilder) -> {
			// Join<UserGatheringId, UserGathering> userGatheringIdJoin = root.join("id", JoinType.INNER);
			return criteriaBuilder.equal(root.get("id").get("user"), user);
		};
	}

	public static Specification<UserGathering> byCompleted(Boolean completed) {
		return (root, query, criteriaBuilder) -> {
			if (completed == null) return criteriaBuilder.conjunction(); // No filtering
			Join<UserGatheringId, Gathering> gatheringJoin = root.join("id").join("gathering", JoinType.INNER);
			LocalDateTime now = LocalDateTime.now();
			return completed
				? criteriaBuilder.lessThan(gatheringJoin.get("dateTime"), now) // 완료된 모임
				: criteriaBuilder.greaterThanOrEqualTo(gatheringJoin.get("dateTime"), now); // 예정된 모임
		};
	}

	public static Specification<UserGathering> byReviewed(Boolean reviewed) {
		return (root, query, criteriaBuilder) -> {
			if (reviewed == null) return criteriaBuilder.conjunction(); // No filtering
			Join<UserGatheringId, Gathering> gatheringJoin = root.join("id").join("gathering", JoinType.INNER);
			Join<Gathering, Review> reviewJoin = gatheringJoin.join("reviews", JoinType.LEFT);
			return reviewed
				? criteriaBuilder.isNotNull(reviewJoin.get("id")) // 리뷰 작성됨
				: criteriaBuilder.isNull(reviewJoin.get("id"));   // 리뷰 미작성
		};
	}
}

