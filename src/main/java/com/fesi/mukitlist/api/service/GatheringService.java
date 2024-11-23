package com.fesi.mukitlist.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.controller.dto.response.GatheringResponse;
import com.fesi.mukitlist.api.domain.Gathering;
import com.fesi.mukitlist.api.domain.UserGathering;
import com.fesi.mukitlist.api.domain.UserGatheringId;
import com.fesi.mukitlist.api.repository.GatheringRepository;
import com.fesi.mukitlist.api.repository.ReviewRepository;
import com.fesi.mukitlist.api.repository.UserGatheringRepository;
import com.fesi.mukitlist.api.service.request.GatheringServiceCreateRequest;
import com.fesi.mukitlist.api.service.request.GatheringServiceRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class GatheringService {

	private final GatheringRepository gatheringRepository;
	private final UserGatheringRepository userGatheringRepository;
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

	public GatheringResponse createGathering(GatheringServiceCreateRequest request) {
		Gathering gathering = Gathering.create(request);
		Gathering savedGathering = gatheringRepository.save(gathering);
		return GatheringResponse.of(savedGathering);
	}


	public GatheringResponse getGatheringById(Long id) {
		Gathering gathering = getGatheringsFrom(id);
		return GatheringResponse.of(gathering);
	}

	public void cancelGathering(Long id, Long userId) {
		Gathering gathering = getGatheringsFrom(id);
		if (!gathering.getCreatedBy().equals(userId)) {
			throw new RuntimeException("Permission denied");
		}

		LocalDateTime canceledTime = LocalDateTime.now();
		gathering.updateCanceledAt(canceledTime);
		gatheringRepository.save(gathering);
	}

	public void joinGathering(Long id, Long userId) {
		Gathering gathering = getGatheringsFrom(id);

		if (gathering.getCanceledAt() != null || gathering.getParticipantCount() >= gathering.getCapacity()) {
			throw new RuntimeException("Cannot join gathering");
		}

		UserGatheringId userGatheringId = UserGatheringId.of(id,userId);
		LocalDateTime joinedTime = LocalDateTime.now();
		UserGathering userGathering = UserGathering.of(userGatheringId, joinedTime);
		userGatheringRepository.save(userGathering);

		gathering.joinParticipant();
	}

	public void leaveGathering(Long id, Long userId) {
		UserGatheringId userGatheringId = UserGatheringId.of(id,userId);
		userGatheringRepository.deleteById(userGatheringId);

		Gathering gathering = getGatheringsFrom(id);
		gathering.leaveParticipant();

		gatheringRepository.save(gathering);
	}

	private Gathering getGatheringsFrom(Long id) {
		return gatheringRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 모임입니다."));
	}

}
