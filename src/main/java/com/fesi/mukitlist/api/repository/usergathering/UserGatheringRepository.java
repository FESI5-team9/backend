package com.fesi.mukitlist.api.repository.usergathering;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.usergathering.UserGathering;
import com.fesi.mukitlist.domain.usergathering.UserGatheringId;

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

}