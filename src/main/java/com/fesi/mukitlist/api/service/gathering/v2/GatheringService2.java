package com.fesi.mukitlist.api.service.gathering.v2;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.repository.gathering.GatheringRepository;
import com.fesi.mukitlist.api.repository.usergathering.UserGatheringRepository;
import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceCreateRequest;
import com.fesi.mukitlist.api.service.gathering.request.GatheringServiceRequest;
import com.fesi.mukitlist.api.service.gathering.response.GatheringListResponse;
import com.fesi.mukitlist.api.service.gathering.response.GatheringResponse;
import com.fesi.mukitlist.api.service.gathering.response.GatheringWithParticipantsResponse;
import com.fesi.mukitlist.api.service.gathering.response.JoinedGatheringsResponse;
import com.fesi.mukitlist.api.service.gathering.response.v2.composition.GatheringCreateResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.gathering.constant.LocationType;
import com.fesi.mukitlist.domain.usergathering.UserGathering;
import com.fesi.mukitlist.api.service.aws.S3Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class GatheringService2 {
	private final GatheringRepository gatheringRepository;
	private final UserGatheringRepository userGatheringRepository;
	private final KeywordService keywordService;
	private final FavoriteService favoriteService;
	private final ParticipationService participationService;
	private final S3Service s3Service;

	@Transactional(readOnly = true)
	public List<GatheringListResponse> getGatherings(GatheringServiceRequest request, Pageable pageable) {
		Page<Gathering> gatherings = gatheringRepository.findWithFilters(request, pageable);
		return response(gatherings, GatheringListResponse::of);
	}

	@Transactional(readOnly = true)
	public List<GatheringListResponse> searchGathering(List<String> keywords, LocationType location, GatheringType type,
		Pageable pageable) {
		Page<Gathering> gatherings = gatheringRepository.searchByTerms(keywords, location, type, pageable);
		return response(gatherings, GatheringListResponse::of);
	}

	public GatheringCreateResponse createGathering(GatheringServiceCreateRequest request, User user) throws
		IOException {

		String imageUrl = s3Service.uploadIfPresent(request.image());
		Gathering gathering = Gathering.create(request, imageUrl, user);

		gatheringRepository.save(gathering);
		List<Keyword> keywords = keywordService.saveKeywords(request.keyword(), gathering);

		return GatheringCreateResponse.of(gathering, keywords);
	}

	@Transactional(readOnly = true)
	public GatheringWithParticipantsResponse getGatheringById(Long gatheringId, User user) {
		Gathering gathering = findGatheringById(gatheringId);

		return GatheringWithParticipantsResponse.of(
			gathering,
			user,
			getKeywordListBy(gathering),
			(user != null) && favoriteService.isFavorite(gathering, user),
			participationService.getParticipants(gathering)
		);
	}

	public GatheringResponse cancelGathering(Long gatheringId, User user) {
		Gathering gathering = findGatheringById(gatheringId);

		validateCancellationAuthority(gathering, user);
		gathering.updateCanceledAt(LocalDateTime.now());

		gatheringRepository.save(gathering);

		return GatheringResponse.of(
			gathering,
			user,
			getKeywordListBy(gathering));
	}

	public void joinGathering(Long gatheringId, User user) {
		Gathering gathering = findGatheringById(gatheringId);
		participationService.joinGathering(gathering, user);
	}

	public void leaveGathering(Long gatheringId, User user, LocalDateTime leaveTime) {
		Gathering gathering = findGatheringById(gatheringId);
		participationService.leaveGathering(gathering, user, leaveTime);
	}

	@Transactional(readOnly = true)
	public List<JoinedGatheringsResponse> getJoinedGatherings(User user, Boolean completed, Boolean reviewed,
		Pageable pageable) {
		Page<UserGathering> userGatherings = userGatheringRepository.findWithFilters(user, completed, reviewed,
			pageable);

		Function<UserGathering, JoinedGatheringsResponse> mapper = ug -> JoinedGatheringsResponse.of(
			ug, completed, reviewed,
			getKeywordListBy(ug.getId().getGathering()));

		return response(userGatherings, mapper);
	}

	public void markAsFavorite(Long gatheringId, User user) {
		Gathering gathering = findGatheringById(gatheringId);
		favoriteService.markAsFavorite(gathering, user);
	}

	public void unmarkAsFavorite(Long gatheringId, User user) {
		Gathering gathering = findGatheringById(gatheringId);
		favoriteService.unmarkAsFavorite(gathering, user);
	}

	private Gathering findGatheringById(Long gatheringId) {
		return gatheringRepository.findById(gatheringId)
			.orElseThrow(() -> new AppException(NOT_FOUND));
	}

	private void validateCancellationAuthority(Gathering gathering, User user) {
		if (!gathering.isCancelAuthorization(user)) {
			throw new AppException(FORBIDDEN);
		}
	}

	private List<Keyword> getKeywordListBy(Gathering gathering) {
		return keywordService.findByGathering(gathering);
	}

	private <T, R> List<R> response(Page<T> page, Function<T, R> mapper) {
		return page.stream()
			.map(mapper)
			.toList();
	}
}



