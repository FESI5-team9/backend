package com.fesi.mukitlist.api.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fesi.mukitlist.api.controller.dto.request.ReviewCreateRequest;
import com.fesi.mukitlist.api.controller.dto.request.ReviewRequest;
import com.fesi.mukitlist.api.domain.GatheringType;
import com.fesi.mukitlist.api.service.ReviewService;
import com.fesi.mukitlist.api.service.response.ReviewResponse;
import com.fesi.mukitlist.api.service.response.ReviewScoreResponse;
import com.fesi.mukitlist.api.service.response.ReviewWithGatheringAndUserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewCreateRequest request) {
		return new ResponseEntity<>(reviewService.createReview(request.toServiceRequest()), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ReviewWithGatheringAndUserResponse>> getReviews(
		ReviewRequest request,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC, size = 10) Pageable pageable
	) {

		return new ResponseEntity<>(reviewService.getReviews(request.toServiceRequest(), pageable), HttpStatus.OK);
	}

	@GetMapping("/score")
	public ResponseEntity<List<ReviewScoreResponse>> getReviewScores(
		@RequestParam(required = false) List<Long> gatheringId,
		@RequestParam(required = false) GatheringType type) {
		return new ResponseEntity<>(reviewService.getReviewScores(gatheringId, type), HttpStatus.OK);

	}
}
