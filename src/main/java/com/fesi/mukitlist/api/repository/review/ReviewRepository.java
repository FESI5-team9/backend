package com.fesi.mukitlist.api.repository.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fesi.mukitlist.api.service.review.request.ReviewServiceRequest;
import com.fesi.mukitlist.domain.Review;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import org.springframework.stereotype.Repository;

@Repository("reviewRepository1")
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
	default Page<Review> findWithFilters(ReviewServiceRequest request, Pageable pageable) {
		Specification<Review> specification = Specification.where(ReviewSpecifications.byFilter(request));

		return findAll(specification, pageable);
	}

	default List<Review> findWithFilters(List<Long> id, GatheringType type) {
		Specification<Review> specification = Specification.where(ReviewSpecifications.ScoreByFilter(id, type));
		return findAll(specification);
	}

	List<Review> findAllByGathering_Type(GatheringType gatheringType);

	Page<Review> findAllByGatheringId(Long gatheringId, Pageable pageable);
}