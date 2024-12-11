package com.fesi.mukitlist.api.controller.mypage;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fesi.mukitlist.api.controller.mypage.response.MyPageReviewResponse;
import com.fesi.mukitlist.domain.service.gathering.response.GatheringListResponse;
import com.fesi.mukitlist.domain.service.mypage.MyPageService;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.api.controller.annotation.Authorize;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Deprecated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/my")
@Tag(name = "My", description = "마이페이지 관련 API")
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping("/gathering")
	public ResponseEntity<List<GatheringListResponse>> getGatheringMyPage(
		@Parameter(hidden = true) @Authorize PrincipalDetails user,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "0") int page,
		@Schema(description = "정렬 기준", example = "dateTime(모임일), registrationEnd(모집 마감일), participantCount(참여 인원)", minimum = "5")
		@RequestParam(defaultValue = "dateTime") String sort,
		@RequestParam(defaultValue = "desc") String direction) {

		Sort sortOrder = Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction)));
		Pageable pageable = PageRequest.of(page, size, sortOrder);
		return new ResponseEntity<>(myPageService.getGatheringMypage(user, pageable), HttpStatus.OK);
	}

	@GetMapping("/review")
	public ResponseEntity<MyPageReviewResponse> getReviewMyPage(
		@Parameter(hidden = true) @Authorize PrincipalDetails user) {
		return new ResponseEntity<>(myPageService.getReviewMypage(user.getUser()),HttpStatus.OK);
	}
}
