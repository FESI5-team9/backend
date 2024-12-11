package com.fesi.mukitlist.api.repository.gathering;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fesi.mukitlist.domain.service.gathering.request.GatheringServiceRequest;
import com.fesi.mukitlist.core.auth.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;

public interface GatheringRepository extends JpaRepository<Gathering, Long>, JpaSpecificationExecutor<Gathering> {
	default Page<Gathering> findWithFilters(GatheringServiceRequest request, Pageable pageable) {
		Specification<Gathering> specification = Specification.where(GatheringSpecifications.byFilter(request));

		return this.findAll(specification, pageable);
	}

	default Page<Gathering> searchByTerms(List<String> searchTerms, LocationType locationType,
		GatheringType gatheringType, Pageable pageable) {
		Specification<Gathering> specification = Specification.where(
			GatheringSpecifications.bySearchTerms(searchTerms, locationType, gatheringType));
		return this.findAll(specification, pageable);
	}

	List<Gathering> findAllByIdIn(List<Long> gatheringId);

	List<Gathering> findGatheringsByUser(User user, Pageable pageable);

	@Query("SELECT g FROM Gathering g WHERE g IN :gatherings AND NOT EXISTS (SELECT 1 FROM Review r WHERE r.gathering = g)")
	List<Gathering> findGatheringsWithoutReviews(@Param("gatherings") List<Gathering> gatherings);

}