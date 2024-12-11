package com.fesi.mukitlist.api.repository.review;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fesi.mukitlist.domain.service.review.request.ReviewServiceRequest;
import com.fesi.mukitlist.core.Review;
import com.fesi.mukitlist.core.auth.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ReviewSpecifications {
	public static Specification<Review> byFilter(ReviewServiceRequest request) {
		return ((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Join<Review, Gathering> gatheringJoin = root.join("gathering", JoinType.INNER);

			Join<Review, User> userJoin = root.join("user", JoinType.INNER);

			if (request.gatheringId() != null) {
				predicates.add(criteriaBuilder.equal(gatheringJoin.get("id"), request.gatheringId()));
			}
			if (request.userId() != null) {
				predicates.add(criteriaBuilder.equal(userJoin.get("id"), request.userId()));
			}
			if (request.type() != null) {
				predicates.add(criteriaBuilder.equal(gatheringJoin.get("type"), request.type()));
			}
			if (request.location() != null) {
				predicates.add(criteriaBuilder.equal(gatheringJoin.get("location"), request.location()));
			}
			if (request.registrationEnd() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(gatheringJoin.get("registrationEnd"),
					request.registrationEnd()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});
	}

	public static Specification<Review> ScoreByFilter(List<Long> id, GatheringType type) {
		return ((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Join<Review, Gathering> gatheringJoin = root.join("gathering", JoinType.INNER);

			if (id != null && !id.isEmpty()) {
				predicates.add(root.get("gathering").get("id").in(id));
			}

			if (type != null) {
				predicates.add(criteriaBuilder.equal(gatheringJoin.get("type"), type));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});
	}
}

