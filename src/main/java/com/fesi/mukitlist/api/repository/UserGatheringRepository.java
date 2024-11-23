package com.fesi.mukitlist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.api.domain.UserGathering;
import com.fesi.mukitlist.api.domain.UserGatheringId;

public interface UserGatheringRepository extends JpaRepository<UserGathering, UserGatheringId> {
}