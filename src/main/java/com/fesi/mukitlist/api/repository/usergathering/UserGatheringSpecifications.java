package com.fesi.mukitlist.api.repository.usergathering;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.fesi.mukitlist.domain.Review;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.usergathering.UserGathering;
import com.fesi.mukitlist.domain.usergathering.UserGatheringId;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class UserGatheringSpecifications {

	public static Specification<UserGathering> byUser(User user) {
		return (root, query, criteriaBuilder) -> {
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
			if (reviewed == null) return criteriaBuilder.conjunction();
			Join<UserGatheringId, Gathering> gatheringJoin = root.join("id").join("gathering", JoinType.INNER);

			Subquery<Long> reviewSubquery = query.subquery(Long.class);
			Root<Review> reviewRoot = reviewSubquery.from(Review.class);
			reviewSubquery.select(reviewRoot.get("gathering").get("id"))
				.where(criteriaBuilder.equal(reviewRoot.get("gathering"), gatheringJoin));

			return reviewed
				? criteriaBuilder.exists(reviewSubquery) // 리뷰 작성됨
				: criteriaBuilder.not(criteriaBuilder.exists(reviewSubquery)); // 리뷰 미작성
		};
	}
}

