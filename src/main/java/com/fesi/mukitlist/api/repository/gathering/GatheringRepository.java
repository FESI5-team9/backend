package com.fesi.mukitlist.api.repository.gathering;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceRequest;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;

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
}