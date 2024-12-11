package com.fesi.mukitlist.api.controller.review;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fesi.mukitlist.api.controller.review.request.ReviewCreateRequest;
import com.fesi.mukitlist.api.controller.review.request.ReviewRequest;
import com.fesi.mukitlist.api.exception.response.ValidationErrorResponse;
import com.fesi.mukitlist.domain.service.review.ReviewService;
import com.fesi.mukitlist.domain.service.review.response.ReviewResponse;
import com.fesi.mukitlist.domain.service.review.response.ReviewScoreResponse;
import com.fesi.mukitlist.domain.service.review.response.ReviewStatisticsScoreResponse;
import com.fesi.mukitlist.domain.service.review.response.ReviewWithGatheringAndUserResponse;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.core.gathering.constant.GatheringType;
import com.fesi.mukitlist.core.gathering.constant.LocationType;
import com.fesi.mukitlist.api.controller.annotation.Authorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "리뷰 관련 API")
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "리뷰 추가", description = "모임에 대한 리뷰를 추가합니다. 사용자는 모임에 참석해야 하고, 해당 모임에 대해 리뷰를 작성한 적이 없어야 합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "리뷰 추가 성공",
				content = @Content(array = @ArraySchema(
					schema = @Schema(implementation = ReviewResponse.class)
				))),
			@ApiResponse(responseCode = "400", description = "잘못된 요청",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))),
		}
	)
	@PostMapping
	public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewCreateRequest request,
		@Parameter(hidden = true) @Authorize PrincipalDetails user) {
		return new ResponseEntity<>(reviewService.createReview(request.toServiceRequest(), user.getUser()),
			HttpStatus.OK);
	}

	@Operation(summary = "리뷰 목록 조회", description = "필터링 및 정렬 조건에 따라 리뷰 목록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공",
				content = @Content(array = @ArraySchema(
					schema = @Schema(implementation = ReviewWithGatheringAndUserResponse.class)
				))),
			@ApiResponse(responseCode = "400", description = "잘못된 요청",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))),
		}
	)
	@GetMapping
	public ResponseEntity<List<ReviewWithGatheringAndUserResponse>> getReviews(
		@RequestParam(required = false) Long gatheringId,
		@RequestParam(required = false) Long userId,
		@RequestParam(required = false) GatheringType type,
		@RequestParam(required = false) LocationType location,
		@RequestParam(required = false) LocalDateTime registrationEnd,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "createdAt") String sort,
		@RequestParam(defaultValue = "desc") String direction
	) {

		ReviewRequest request = ReviewRequest.of(gatheringId, userId, type, location, registrationEnd, size,
			page, sort, direction);
		return new ResponseEntity<>(reviewService.getReviews(request.toServiceRequest()), HttpStatus.OK);
	}
	@Operation(summary = "리뷰 평점 목록 조회", description = "필터링에 따라 리뷰 평점 목록을 조회합니다",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공",
				content = @Content(array = @ArraySchema(
					schema = @Schema(implementation = ReviewScoreResponse.class)
				))),
		}
	)
	@GetMapping("/score")
	public ResponseEntity<List<ReviewScoreResponse>> getReviewScores(
		@RequestParam(required = false) List<Long> gatheringId,
		@RequestParam(required = false) GatheringType type) {
		return new ResponseEntity<>(reviewService.getReviewScores(gatheringId, type), HttpStatus.OK);
	}

	@Operation(summary = "타입별 리뷰 평점 통계 조회", description = "타입별 리뷰 평점 통계를 조회합니다",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공",
				content = @Content(array = @ArraySchema(
					schema = @Schema(implementation = ReviewStatisticsScoreResponse.class)
				))),
		}
	)
	@GetMapping("/statistics")
	public ResponseEntity<ReviewStatisticsScoreResponse> getReviewScores(
		@RequestParam(required = false) GatheringType type) {
		return new ResponseEntity<>(reviewService.getReviewScoreStatistics(type), HttpStatus.OK);
	}

}
