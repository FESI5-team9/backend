package com.fesi.mukitlist.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.domain.gathering.favorite.FavoriteGathering;
import com.fesi.mukitlist.domain.gathering.favorite.FavoriteGatheringId;

public interface FavoriteGatheringRepository extends JpaRepository<FavoriteGathering, FavoriteGatheringId> {
	List<FavoriteGathering> findById_UserId(Long userId);

}
