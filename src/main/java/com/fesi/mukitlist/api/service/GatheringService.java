package com.fesi.mukitlist.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.domain.Gathering;
import com.fesi.mukitlist.api.domain.Review;
import com.fesi.mukitlist.api.domain.User;
import com.fesi.mukitlist.api.domain.UserGathering;
import com.fesi.mukitlist.api.domain.UserGatheringId;
import com.fesi.mukitlist.api.repository.GatheringRepository;
import com.fesi.mukitlist.api.repository.ReviewRepository;
import com.fesi.mukitlist.api.repository.UserGatheringRepository;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.api.service.request.GatheringServiceCreateRequest;
import com.fesi.mukitlist.api.service.request.GatheringServiceRequest;
import com.fesi.mukitlist.api.service.response.GatheringParticipantsResponse;
import com.fesi.mukitlist.api.service.response.GatheringResponse;
import com.fesi.mukitlist.api.service.response.JoinedGatheringsResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class GatheringService {

	private final GatheringRepository gatheringRepository;
	private final UserGatheringRepository userGatheringRepository;
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public List<GatheringResponse> getGatherings(GatheringServiceRequest request, Pageable pageable) {

		Specification<Gathering> specification = ((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(criteriaBuilder.isNull(root.get("canceledAt")));

			if (request.id() != null && !request.id().isEmpty()) {
				predicates.add(root.get("id").in(request.id()));
			}

			if (request.type() != null) {
				predicates.add(criteriaBuilder.equal(root.get("type"), request.type()));
			}

			if (request.location() != null) {
				predicates.add(criteriaBuilder.equal(root.get("location"), request.location()));
			}

			if (request.dateTime() != null) {
				predicates.add(criteriaBuilder.between(root.get("dateTime"), request.dateTime().toLocalDate()
					.atStartOfDay(), request.dateTime().plusDays(1).toLocalDate().atStartOfDay()));
			}

			if (request.createdBy() != null) {
				predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.createdBy()));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});

		Page<Gathering> gatheringPage = gatheringRepository.findAll(specification, pageable);

		return gatheringPage.stream()
			.map(GatheringResponse::of)
			.toList();
	}

	public GatheringResponse createGathering(GatheringServiceCreateRequest request, LocalDateTime now) {
		Gathering gathering = Gathering.create(request, now);
		Gathering savedGathering = gatheringRepository.save(gathering);
		return GatheringResponse.of(savedGathering);
	}

	@Transactional(readOnly = true)
	public GatheringResponse getGatheringById(Long id) {
		Gathering gathering = getGatheringsFrom(id);
		return GatheringResponse.of(gathering);
	}

	public GatheringResponse cancelGathering(Long id) {

		User user = getUserFrom(1L);
		Gathering gathering = getGatheringsFrom(id);
		if (!gathering.getCreatedBy().equals(user)) {
			throw new RuntimeException("Permission denied");
		}

		LocalDateTime canceledTime = LocalDateTime.now();
		gathering.updateCanceledAt(canceledTime);
		Gathering savedGathering = gatheringRepository.save(gathering);

		return GatheringResponse.of(savedGathering);
	}

	public void joinGathering(Long id) {
		Gathering gathering = getGatheringsFrom(id);
		User user = getUserFrom(1L);
		if (gathering.getCanceledAt() != null || gathering.getParticipantCount() >= gathering.getCapacity()) {
			throw new RuntimeException("Cannot join gathering");
		}

		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		LocalDateTime joinedTime = LocalDateTime.now();
		UserGathering userGathering = UserGathering.of(userGatheringId, joinedTime);
		userGatheringRepository.save(userGathering);

		gathering.joinParticipant();
	}

	public void leaveGathering(Long id) {
		Gathering gathering = getGatheringsFrom(id);
		User user = getUserFrom(1L);
		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		userGatheringRepository.deleteById(userGatheringId);

		gathering.leaveParticipant();
		gatheringRepository.save(gathering);
	}

	public List<JoinedGatheringsResponse> getJoinedGatherings(Boolean completed, Boolean reviewed, Pageable pageable) {
		User user = getUserFrom(1L);

		Specification<UserGathering> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			Join<UserGatheringId,UserGathering> userGatheringIdJoin = root.join("id", JoinType.INNER);

			predicates.add(criteriaBuilder.equal(userGatheringIdJoin.get("user"), user));

			Join<UserGatheringId, Gathering> gatheringJoin = userGatheringIdJoin.join("gathering", JoinType.INNER);

			if (completed != null) {
				LocalDateTime now = LocalDateTime.now();
				if (completed) {
					predicates.add(criteriaBuilder.lessThan(gatheringJoin.get("dateTime"), now));
				} else {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(gatheringJoin.get("dateTime"), now));
				}
			}

			if (reviewed != null) {
				Join<Gathering, Review> reviewJoin = gatheringJoin.join("reviews", JoinType.LEFT);
				if (reviewed) {
					predicates.add(criteriaBuilder.isNotNull(reviewJoin.get("id")));
				} else {
					predicates.add(criteriaBuilder.isNull(reviewJoin.get("id")));
				}
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		Page<UserGathering> userGatheringsPage = userGatheringRepository.findAll(specification, pageable);
		return userGatheringsPage.stream()
			.map(JoinedGatheringsResponse::of)
			.toList();
	}

	public List<GatheringParticipantsResponse> getGatheringParticipants(Long id, Pageable pageable) {
		List<UserGathering> gatherings = userGatheringRepository.findByIdGatheringId(id, pageable).getContent();
		return gatherings.stream()
			.map(GatheringParticipantsResponse::of)
			.toList();
	}

	private Gathering getGatheringsFrom(Long id) {
		return gatheringRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 모임입니다."));
	}


	private User getUserFrom(Long id) {
		return userRepository.findById(id).orElse(null);
	}
}
