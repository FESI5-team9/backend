package com.fesi.mukitlist.api.repository.gathering;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceRequest;
import com.fesi.mukitlist.domain.gathering.Gathering;

import jakarta.persistence.criteria.Predicate;

public class GatheringSpecifications {
	public static Specification<Gathering> byFilter(GatheringServiceRequest request) {
		Specification<Gathering> specification = ((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(criteriaBuilder.isNull(root.get("canceledAt")));

			if (request.id() != null && !request.id().isEmpty()) {
				predicates.add(root.get("id").in(request.id()));
			}

			if (request.type() != null) {
				predicates.add(criteriaBuilder.equal(root.get("type"), request.type()));
			}

			if (request.location() != null) {
				predicates.add(criteriaBuilder.equal(root.get("location"), request.location()));
			}

			if (request.dateTime() != null) {
				predicates.add(criteriaBuilder.between(root.get("dateTime"), request.dateTime().toLocalDate()
					.atStartOfDay(), request.dateTime().plusDays(1).toLocalDate().atStartOfDay()));
			}

			if (request.createdBy() != null) {
				predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.createdBy()));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});
		return specification;
	}
}
