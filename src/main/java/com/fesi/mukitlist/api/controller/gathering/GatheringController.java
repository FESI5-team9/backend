package com.fesi.mukitlist.api.controller.gathering;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.fesi.mukitlist.api.controller.gathering.request.GatheringCreateRequest;
import com.fesi.mukitlist.api.controller.gathering.request.GatheringRequest;
import com.fesi.mukitlist.api.exception.response.ValidationErrorResponse;
import com.fesi.mukitlist.api.service.gathering.GatheringService;
import com.fesi.mukitlist.api.service.gathering.response.GatheringParticipantsResponse;
import com.fesi.mukitlist.api.service.gathering.response.GatheringResponse;
import com.fesi.mukitlist.api.service.gathering.response.JoinedGatheringsResponse;
import com.fesi.mukitlist.domain.gathering.GatheringType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gatherings")
@Tag(name = "Gatherings", description = "모임 관련 API")
@Slf4j
public class GatheringController {
	private final GatheringService gatheringService;

	@Operation(summary = "모임 목록 조회", description = "모임의 종류, 위치, 날짜 등 다양한 조건으로 모임 목록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 목록 조회 성공",
				content = @Content(array = @ArraySchema(
					schema = @Schema(implementation = GatheringResponse.class)
				))),
			@ApiResponse(responseCode = "400", description = "잘못된 요청",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))),
		}
	)
	@GetMapping
	ResponseEntity<List<GatheringResponse>> getGatherings(
		@RequestParam(required = false) List<Long> id,
		@RequestParam(required = false) GatheringType type,
		@RequestParam(required = false) LocalDateTime dateTime,
		@RequestParam(required = false) String location,
		@RequestParam(required = false) String createdBy,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "dateTime") String sort,
		@RequestParam(defaultValue = "ASC") String direction
	) {
		
		GatheringRequest request = GatheringRequest.of(id, type, dateTime, location, createdBy,size, page, sort, direction);
		List<GatheringResponse> gatheringResponseDtoList = gatheringService.getGatherings(request.toServiceRequest());

		return new ResponseEntity<>(gatheringResponseDtoList, HttpStatus.OK);
	}

	@Operation(summary = "모임 상세 조회", description = "모임의 상제 정보를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 상세 조회 성공",
				content = @Content(schema = @Schema(implementation = GatheringResponse.class))),
			@ApiResponse(
				responseCode = "400",
				description = "요청 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"VALIDATION_ERROR\",\"parameter\":\"id\",\"message\":\"유효한 모임 ID를 입력하세요\"}"
					)
				)
			),
			@ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음",
				content = @Content(schema = @Schema(
					example = "{\"code\":\"NOT_FOUND\",\"message\":\"모임을 찾을 수 없습니다\"}"
				))),
		}
	)
	@GetMapping("/{id}")
	ResponseEntity<GatheringResponse> getGatheringById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(gatheringService.getGatheringById(id), HttpStatus.OK);
	}

	@Operation(summary = "특정 모임의 참가자 목록 조회", description = "특정 모임의 참가자 목록을 페이지네이션 하여 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 목록 조회 성공",
				content = @Content(
					mediaType = "application/json",
					array = @ArraySchema(
						schema = @Schema(implementation = GatheringParticipantsResponse.class)
					)
				)),
			@ApiResponse(
				responseCode = "400",
				description = "요청 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"VALIDATION_ERROR\",\"parameter\":\"id\",\"message\":\"유효한 모임 ID를 입력하세요\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "모임 없음",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"NOT_FOUND\",\"message\":\"모임을 찾을 수 없습니다.\"}"
					)
				)
			),
		}
	)
	@GetMapping("/{id}/participants")
	ResponseEntity<List<GatheringParticipantsResponse>> getGatheringParticipantsById(
		@PathVariable("id") Long id,
		@RequestParam(defaultValue = "5") int size,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "joinedAt") String sort,
		@RequestParam(defaultValue = "ASC") String direction) {

		Sort sortOrder = Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction)));
		Pageable pageable = PageRequest.of(page, size, sortOrder);
		return new ResponseEntity<>(gatheringService.getGatheringParticipants(id, pageable), HttpStatus.OK);
	}

	@Operation(summary = "모임 생성", description = "새로운 모임 생성",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 생성 성공",
				content = @Content(schema = @Schema(implementation = GatheringResponse.class))),
			@ApiResponse(responseCode = "400", description = "잘못된 요청",
				content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class))),
		}
	)
	@PostMapping
	public ResponseEntity<GatheringResponse> createGathering(
		@Valid @RequestBody GatheringCreateRequest gatheringCreateRequest) {
		GatheringResponse gathering = gatheringService.createGathering(gatheringCreateRequest.toServiceRequest());
		return new ResponseEntity<>(gathering, HttpStatus.CREATED);
	}

	@Operation(summary = "로그인된 사용자가 참석한 모임 목록 조회", description = "로그인된 사용자가 참석한 모임의 목록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 목록 조회 성공",
				content = @Content(schema = @Schema(implementation = JoinedGatheringsResponse.class))),
			@ApiResponse(
				responseCode = "400",
				description = "요청 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"VALIDATION_ERROR\",\"parameter\":\"size\",\"message\":\"size는 최소 1이어야 합니다.\"}"
					)
				)
			),
		}
	)

	@GetMapping("/joined")
	public ResponseEntity<List<JoinedGatheringsResponse>> getGatheringsBySignInUser(
		// @AuthenticationPrincipal User user,
		@RequestParam(required = false) Long userId,
		@RequestParam(required = false) Boolean completed,
		@RequestParam(required = false) Boolean reviewed,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "id.gathering.dateTime") String sort,
		@RequestParam(defaultValue = "ASC") String direction) {

		Sort sortOrder = Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction)));
		Pageable pageable = PageRequest.of(page, size, sortOrder);
		List<JoinedGatheringsResponse> repsonse = gatheringService.getJoinedGatherings(userId, completed, reviewed, pageable);
		return new ResponseEntity<>(repsonse, HttpStatus.OK);
	}

	@Operation(summary = "모임 취소", description = "모임을 취소합니다. 모임 생성자만 취소할 수 있습니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 취소 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = GatheringResponse.class))),
			@ApiResponse(
				responseCode = "403",
				description = "권한 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"FORBIDDEN\",\"message\":\"모임을 취소할 권한이 없습니다\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "모임 없음",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"NOT_FOUND\",\"message\":\"모임을 찾을 수 없습니다.\"}"
					)
				)
			),
		}
	)
	@PutMapping("/{id}/cancel")
	public ResponseEntity<GatheringResponse> cancelGathering(@PathVariable("id") Long id) {
		return new ResponseEntity<>(gatheringService.cancelGathering(id), HttpStatus.OK);
	}

	@Operation(summary = "모임 참여", description = "로그인한 사용자가 모임에 참여합니다",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 목록 조회 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"message\":\"모임에 참여했습니다.\"}"
					)
				)),
			@ApiResponse(
				responseCode = "400",
				description = "참여 불가 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"GATHERING_CANCELED\",\"message\":\"취소된 모임입니다.\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "모임 없음",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"NOT_FOUND\",\"message\":\"모임을 찾을 수 없습니다.\"}"
					)
				)
			),
		}
	)
	@PostMapping("/{id}/join")
	public ResponseEntity<Map<String, String>> joinGathering(@PathVariable("id") Long id) {
		gatheringService.joinGathering(id);
		return new ResponseEntity<>(Map.of("message", "모임에 참여했습니다."), HttpStatus.OK);
	}

	@Operation(summary = "모임 참여 취소", description = "사용자가 모임에서 참여 취소합니다.이미 지난 모임은 참여 취소가 불가합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "모임 목록 조회 성공",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"message\":\"모임에 참여했습니다.\"}"
					)
				)),
			@ApiResponse(
				responseCode = "400",
				description = "참여 불가 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"PAST_GATHERING\",\"message\":\"이미 지난 모임입니다\"}"
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "모임 없음",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						example = "{\"code\":\"NOT_FOUND\",\"message\":\"모임을 찾을 수 없습니다.\"}"
					)
				)
			),
		}
	)
	@DeleteMapping("/{id}/leave")
	public ResponseEntity<Map<String, String>> leaveGathering(@PathVariable("id") Long id) {
		LocalDateTime leaveTime = LocalDateTime.now();
		gatheringService.leaveGathering(id, leaveTime);
		return new ResponseEntity(Map.of("message", "모임을 참여 취소했습니다"), HttpStatus.OK);
	}

}
