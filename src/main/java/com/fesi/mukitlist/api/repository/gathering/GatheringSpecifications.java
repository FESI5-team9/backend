package com.fesi.mukitlist.api.repository.gathering;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceRequest;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class GatheringSpecifications {
	public static Specification<Gathering> byFilter(GatheringServiceRequest request) {
		return ((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			LocalDateTime now = LocalDateTime.now();

			predicates.add(criteriaBuilder.isNull(root.get("canceledAt")));
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateTime"), now));

			if (request.id() != null && !request.id().isEmpty()) {
				predicates.add(root.get("id").in(request.id()));
			}

			if (request.type() != null) {
				predicates.add(criteriaBuilder.equal(root.get("type"), request.type()));
			}

			if (request.location() != null) {
				predicates.add(criteriaBuilder.equal(root.get("location"), request.location()));
			}

			if (request.startDate() != null && request.endDate() != null) {
				predicates.add(criteriaBuilder.between(root.get("dateTime"), request.startDate()
					.atStartOfDay(), request.endDate().atStartOfDay()));
			}

			if (request.createdBy() != null) {
				predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.createdBy()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});
	}

	public static Specification<Gathering> bySearchTerms(List<String> searchTerms, LocationType locationType, GatheringType gatheringType) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (locationType != null) {
				predicates.add(criteriaBuilder.equal(root.get("location"), locationType));
			}

			if (gatheringType != null) {
				predicates.add(criteriaBuilder.equal(root.get("type"), gatheringType));
			}

			if (searchTerms != null && !searchTerms.isEmpty()) {
				List<Predicate> searchPredicates = searchTerms.stream().map(term -> {
					String searchTerm = "%" + term + "%"; // Like 연산자용

					Predicate namePredicate = criteriaBuilder.like(root.get("name"), searchTerm);
					Predicate address1Predicate = criteriaBuilder.like(root.get("address1"), searchTerm);
					Predicate address2Predicate = criteriaBuilder.like(root.get("address2"), searchTerm);
					Predicate descriptionPredicate = criteriaBuilder.like(root.get("description"), searchTerm);

					Subquery<Long> keywordSubquery = query.subquery(Long.class);
					Root<Keyword> keywordRoot = keywordSubquery.from(Keyword.class);

					keywordSubquery.select(keywordRoot.get("gathering").get("id"))
						.where(criteriaBuilder.and(
							criteriaBuilder.equal(keywordRoot.get("gathering").get("id"), root.get("id")),
							criteriaBuilder.like(keywordRoot.get("keyword"), searchTerm)
						));

					return criteriaBuilder.or(
						namePredicate,
						address1Predicate,
						address2Predicate,
						descriptionPredicate,
						criteriaBuilder.exists(keywordSubquery)
					);
				}).collect(Collectors.toList());

				// 검색어 조건을 OR로 묶음
				predicates.add(criteriaBuilder.or(searchPredicates.toArray(new Predicate[0])));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
