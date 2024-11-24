package com.fesi.mukitlist.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.api.domain.UserGathering;
import com.fesi.mukitlist.api.domain.UserGatheringId;

public interface UserGatheringRepository extends JpaRepository<UserGathering, UserGatheringId> {
	Page<UserGathering> findAll(Specification<UserGathering> specification, Pageable pageable);

	Page<UserGathering> findByIdGatheringId(Long id, Pageable pageable);

}