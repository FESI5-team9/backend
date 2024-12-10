package com.fesi.mukitlist.api.service.review;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.api.repository.review.ReviewRepository;
import com.fesi.mukitlist.api.repository.usergathering.UserGatheringRepository;
import com.fesi.mukitlist.api.service.PageService;
import com.fesi.mukitlist.api.service.review.response.ReviewStatisticsScoreResponse;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.constant.GatheringType;
import com.fesi.mukitlist.domain.Review;
import com.fesi.mukitlist.api.repository.gathering.GatheringRepository;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.api.service.review.request.ReviewServiceCreateRequest;
import com.fesi.mukitlist.api.service.review.request.ReviewServiceRequest;
import com.fesi.mukitlist.api.service.review.response.ReviewResponse;
import com.fesi.mukitlist.api.service.review.response.ReviewScoreResponse;
import com.fesi.mukitlist.api.service.review.response.ReviewWithGatheringAndUserResponse;
import com.fesi.mukitlist.domain.usergathering.UserGatheringId;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final GatheringRepository gatheringRepository;
	private final UserGatheringRepository userGatheringRepository;

	@Transactional(readOnly = true)
	public List<ReviewWithGatheringAndUserResponse> getReviews(ReviewServiceRequest request) {

		Pageable pageable = PageService.pageableBy(request.page(), request.size(), request.sort(),
			request.direction());
		Page<Review> reviewsPage = reviewRepository.findWithFilters(request, pageable);

		return reviewsPage.stream()
			.map(ReviewWithGatheringAndUserResponse::of)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<ReviewResponse> getReviewsBy(ReviewServiceRequest request) {

		Pageable pageable = PageService.pageableBy(request.page(), request.size(), request.sort(),
			request.direction());
		Page<Review> reviewsPage = reviewRepository.findAllByGatheringId(request.gatheringId(), pageable);

		return reviewsPage.stream()
			.map(ReviewResponse::of)
			.toList();
	}

	public ReviewResponse createReview(ReviewServiceCreateRequest request, User user) {

		Gathering gathering = getGatheringsFrom(request.gatheringId());
		checkIsUserParticipant(user, gathering);

		Review savedReview = reviewRepository.save(request.toEntity(gathering, user));

		return ReviewResponse.of(savedReview);
	}

	@Transactional(readOnly = true)
	public List<ReviewScoreResponse> getReviewScores(List<Long> id, GatheringType type) {

		List<Review> reviewsPage = reviewRepository.findWithFilters(id, type);
		Map<Gathering, Map<Integer, Long>> scoreCountsByGatheringId = reviewsPage.stream()
			.collect(Collectors.groupingBy(
				Review::getGathering,
				Collectors.groupingBy(
					Review::getScore,
					Collectors.counting()
				)
			));

		return scoreCountsByGatheringId.entrySet().stream()
			.map(entry -> {
				Gathering gathering = entry.getKey();

				Map<Integer, Long> scoreCounts = entry.getValue();
				long totalScore = getTotalScore(scoreCounts);
				long totalReviews = getTotalReviewSum(scoreCounts);
				double averageScore = getAverageScore(totalReviews, (double)totalScore);

				return ReviewScoreResponse.of(gathering, averageScore, scoreCounts);
			})
			.toList();
	}

	@Transactional(readOnly = true)
	public ReviewStatisticsScoreResponse getReviewScoreStatistics(GatheringType type) {
		List<Review> reviewCandidates = reviewRepository.findAllByGathering_Type(type);

		Map<Integer, Long> reviewScoreCounts = reviewCandidates.stream()
			.collect(Collectors.groupingBy(
				Review::getScore,
				Collectors.counting()
			));

		long totalScore = getTotalScore(reviewScoreCounts);
		long totalReviews = getTotalReviewSum(reviewScoreCounts);
		double averageScore = getAverageScore(totalReviews, (double)totalScore);

		return ReviewStatisticsScoreResponse.of(averageScore, reviewScoreCounts);

	}

	private long getTotalReviewSum(Map<Integer, Long> reviewScoreCounts) {
		return reviewScoreCounts.values().stream()
			.mapToLong(Long::longValue)
			.sum();
	}

	private long getTotalScore(Map<Integer, Long> reviewScoreCounts) {
		return reviewScoreCounts.entrySet().stream()
			.mapToLong(e -> e.getKey() * e.getValue())
			.sum();
	}

	private double getAverageScore(long totalReviews, double totalScore) {
		return totalReviews > 0 ? totalScore / totalReviews : 0.0;
	}

	private void checkIsUserParticipant(User user, Gathering gathering) {
		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		if (!userGatheringRepository.existsById(userGatheringId)) {
			throw new AppException(NOT_PARTICIPANTS);
		}
	}

	private Gathering getGatheringsFrom(Long id) {
		return gatheringRepository.findById(id).orElseThrow(() -> new AppException(NOT_FOUND));
	}

}
