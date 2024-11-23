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
import org.springframework.web.bind.annotation.RestController;

import com.fesi.mukitlist.api.controller.dto.request.GatheringCreateRequest;
import com.fesi.mukitlist.api.controller.dto.request.GatheringRequest;
import com.fesi.mukitlist.api.controller.dto.response.GatheringResponse;
import com.fesi.mukitlist.api.service.GatheringService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gatherings")
public class GatheringController {
	private final GatheringService gatheringService;

	@GetMapping
	ResponseEntity<List<GatheringResponse>> getGatherings(
		GatheringRequest request,
		@PageableDefault(
			sort = "dateTime",
			direction = Sort.Direction.ASC,
			size = 10
		)
		Pageable pageable
	) {

		List<GatheringResponse> gatheringResponseDtoList = gatheringService.getGatherings(request.toServiceRequest(),pageable);

		return new ResponseEntity<>(gatheringResponseDtoList, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<GatheringResponse> createGathering(@RequestBody GatheringCreateRequest gatheringCreateRequest) {
		GatheringResponse gathering = gatheringService.createGathering(gatheringCreateRequest.toServiceRequest());
		return new ResponseEntity<>(gathering, HttpStatus.CREATED);
	}

}
