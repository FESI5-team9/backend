package com.fesi.mukitlist.api.service.gathering.v2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.repository.FavoriteGatheringRepository;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.favorite.FavoriteGathering;
import com.fesi.mukitlist.domain.gathering.favorite.FavoriteGatheringId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class FavoriteService {

	private final FavoriteGatheringRepository favoriteGatheringRepository;

	public boolean isFavorite(Gathering gathering, User user) {
		boolean isFavorite = false;
		if (user != null) {
			isFavorite = checkIsFavoriteGathering(gathering, user);
		}
		return isFavorite;
	}

	private boolean checkIsFavoriteGathering(Gathering gathering, User user) {
		return favoriteGatheringRepository.existsById(FavoriteGatheringId.of(user.getId(), gathering.getId()));
	}

	public void markAsFavorite(Gathering gathering, User user) {
		favoriteGatheringRepository.save(
			FavoriteGathering.of(FavoriteGatheringId.of(user.getId(), gathering.getId())));
	}

	public void unmarkAsFavorite(Gathering gathering, User user) {
		favoriteGatheringRepository.delete(
			FavoriteGathering.of(FavoriteGatheringId.of(user.getId(), gathering.getId())));
	}
}
