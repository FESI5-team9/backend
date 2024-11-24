package com.fesi.mukitlist.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fesi.mukitlist.api.controller.dto.request.GatheringCreateRequest;
import com.fesi.mukitlist.api.controller.dto.request.GatheringRequest;
import com.fesi.mukitlist.api.service.response.GatheringParticipantsResponse;
import com.fesi.mukitlist.api.service.response.GatheringResponse;
import com.fesi.mukitlist.api.service.GatheringService;
import com.fesi.mukitlist.api.service.response.JoinedGatheringsResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gatherings")
public class GatheringController {
	private final GatheringService gatheringService;

	@GetMapping
	ResponseEntity<List<GatheringResponse>> getGatherings(
		GatheringRequest request,
		@PageableDefault(sort = "dateTime", direction = Sort.Direction.ASC, size = 10) Pageable pageable
	) {

		List<GatheringResponse> gatheringResponseDtoList = gatheringService.getGatherings(request.toServiceRequest(),
			pageable);

		return new ResponseEntity<>(gatheringResponseDtoList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	ResponseEntity<GatheringResponse> getGatheringById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(gatheringService.getGatheringById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}/participants")
	ResponseEntity<List<GatheringParticipantsResponse>> getGatheringParticipantsById(
		@PathVariable("id") Long id,
		@PageableDefault(sort = "joinedAt", direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
		return new ResponseEntity<>(gatheringService.getGatheringParticipants(id, pageable), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<GatheringResponse> createGathering(
		@RequestBody GatheringCreateRequest gatheringCreateRequest) {
		LocalDateTime canceledAt = LocalDateTime.now();
		GatheringResponse gathering = gatheringService.createGathering(gatheringCreateRequest.toServiceRequest(), canceledAt);
		return new ResponseEntity<>(gathering, HttpStatus.CREATED);
	}

	@GetMapping("/joined")
	public ResponseEntity<List<JoinedGatheringsResponse>> getGatheringsBySignInUser(
		@RequestParam(required = false) Boolean completed,
		@RequestParam(required = false) Boolean reviews,
		@PageableDefault(sort = "id.gathering.dateTime", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

		List<JoinedGatheringsResponse> repsonse = gatheringService.getJoinedGatherings(completed, reviews, pageable);
		return new ResponseEntity<>(repsonse, HttpStatus.OK);
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<GatheringResponse> cancelGathering(@PathVariable("id") Long id) {
		return new ResponseEntity<>(gatheringService.cancelGathering(id), HttpStatus.OK);
	}

	@PostMapping("/{id}/join")
	public ResponseEntity joinGathering(@PathVariable("id") Long id) {
		gatheringService.joinGathering(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}/leave")
	public ResponseEntity leaveGathering(@PathVariable("id") Long id) {
		gatheringService.leaveGathering(id);
		return new ResponseEntity(HttpStatus.OK);
	}

}
