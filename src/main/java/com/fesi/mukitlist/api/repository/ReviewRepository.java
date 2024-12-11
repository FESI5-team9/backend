package com.fesi.mukitlist.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fesi.mukitlist.domain.Review;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import org.springframework.stereotype.Repository;

@Repository("reviewRepository2")
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
	List<Review> findAllByGathering_Type(GatheringType gatheringType);

	Page<Review> findAllByGatheringId(Long gatheringId, Pageable pageable);

	List<Review> findAllByUser(User user);
}

