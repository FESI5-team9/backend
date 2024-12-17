package com.fesi.mukitlist.core.repository.usergathering;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.usergathering.UserGathering;
import com.fesi.mukitlist.core.usergathering.UserGatheringId;

public interface UserGatheringRepository
	extends JpaRepository<UserGathering, UserGatheringId>, JpaSpecificationExecutor<UserGathering> {
	default Page<UserGathering> findWithFilters(User user, Boolean completed, Boolean reviewed, Pageable pageable) {
		Specification<UserGathering> specification = Specification.where(UserGatheringSpecifications.byUser(user))
			.and(UserGatheringSpecifications.byCompleted(completed))
			.and(UserGatheringSpecifications.byReviewed(reviewed));

		return this.findAll(specification, pageable);
	}

	Page<UserGathering> findByIdGathering(Gathering gathering, Pageable pageable);

	List<UserGathering> findByIdGathering(Gathering gathering);

	List<UserGathering> findByIdUser(User user);

	@Query("SELECT ug.id.gathering FROM UserGathering ug WHERE ug.id.user = :user AND NOT EXISTS (SELECT 1 FROM Review r WHERE r.gathering = ug.id.gathering)")
	List<Gathering> findGatheringsWithoutReviewsByUser(@Param("user") User user);

}