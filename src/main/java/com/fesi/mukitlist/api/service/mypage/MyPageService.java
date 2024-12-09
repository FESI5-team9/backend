package com.fesi.mukitlist.api.service.mypage;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.controller.mypage.response.MyPageReviewResponse;
import com.fesi.mukitlist.api.controller.mypage.response.ReviewCompletedList;
import com.fesi.mukitlist.api.controller.mypage.response.ReviewUnCompletedList;
import com.fesi.mukitlist.api.repository.ReviewRepository;
import com.fesi.mukitlist.api.repository.gathering.GatheringRepository;
import com.fesi.mukitlist.api.repository.usergathering.UserGatheringRepository;
import com.fesi.mukitlist.api.service.gathering.response.GatheringListResponse;
import com.fesi.mukitlist.api.service.gathering.response.GatheringReviewResponse;
import com.fesi.mukitlist.api.service.review.response.ReviewResponse;
import com.fesi.mukitlist.domain.Review;
import com.fesi.mukitlist.domain.auth.PrincipalDetails;
import com.fesi.mukitlist.domain.auth.User;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.usergathering.UserGathering;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MyPageService {
	private final UserGatheringRepository userGatheringRepository;
	private final GatheringRepository gatheringRepository;
	private final ReviewRepository reviewRepository;

	public List<GatheringListResponse> getGatheringMypage(PrincipalDetails user, Pageable pageable) {
		List<Gathering> response  = gatheringRepository.findGatheringsByUser(user.getUser(), pageable);
		return response.stream()
			.map(GatheringListResponse::of)
			.toList();
	}

	public MyPageReviewResponse getReviewMypage(User user) {
		List<Review> reviews = reviewRepository.findAllByUser(user);
		List<ReviewCompletedList> reviewCompletedLists = reviews.stream()
			.map(r -> ReviewCompletedList.of(GatheringListResponse.of(r.getGathering()), ReviewResponse.of(r)))
			.collect(Collectors.toList());

		List<UserGathering> userGatherings = userGatheringRepository.findByIdUser(user);
		List<Gathering> gatherings = userGatherings.stream().map(ug -> ug.getId().getGathering()).toList();
		List<ReviewUnCompletedList> reviewUnCompletedLists = gatheringRepository.findGatheringsWithoutReviews(
				gatherings).stream()
			.map(g -> ReviewUnCompletedList.of(GatheringListResponse.of(g))).toList();

		return MyPageReviewResponse.of(reviewCompletedLists, reviewUnCompletedLists);

	}
}
