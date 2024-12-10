package com.fesi.mukitlist.api.service.gathering.v2;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.repository.usergathering.UserGatheringRepository;
import com.fesi.mukitlist.api.service.gathering.response.GatheringParticipantsResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.usergathering.UserGathering;
import com.fesi.mukitlist.domain.usergathering.UserGatheringId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ParticipationService {
	private final UserGatheringRepository userGatheringRepository;



	public Gathering joinGathering(Gathering gathering, User user) {
		checkIsCanceledGathering(gathering);
		checkIsJoinedGathering(gathering);

		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		LocalDateTime joinedTime = LocalDateTime.now();
		UserGathering userGathering = UserGathering.of(userGatheringId, joinedTime);
		userGatheringRepository.save(userGathering);

		gathering.joinParticipant();
		return gathering;
	}

	public Gathering leaveGathering(Gathering gathering, User user, LocalDateTime leaveTime) {
		checkIsNotPastGathering(gathering, leaveTime);

		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		UserGathering userGathering = userGatheringRepository.findById(userGatheringId)
			.orElseThrow(() -> new AppException(NOT_PARTICIPANTS));
		userGatheringRepository.delete(userGathering);
		gathering.leaveParticipant();

		return gathering;
	}

	private void checkIsNotPastGathering(Gathering gathering, LocalDateTime leaveTime) {
		if (leaveTime.isAfter(gathering.getDateTime())) {
			throw new AppException(PAST_GATHERING);
		}
	}

	private void checkIsCanceledGathering(Gathering gathering) {
		if (gathering.isCanceledGathering()) {
			throw new AppException(GATHERING_CANCELED);
		}
	}

	private void checkIsJoinedGathering(Gathering gathering) {
		if (!gathering.isJoinableGathering()) {
			throw new AppException(MAXIMUM_PARTICIPANTS);
		}
	}

	public List<GatheringParticipantsResponse> getParticipants(Gathering gathering) {
		return userGatheringRepository.findByIdGathering(gathering).stream()
			.map(GatheringParticipantsResponse::of
			)
			.toList();

	}
}
