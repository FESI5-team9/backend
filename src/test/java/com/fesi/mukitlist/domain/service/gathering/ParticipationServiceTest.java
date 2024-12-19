package com.fesi.mukitlist.domain.service.gathering;

import static com.fesi.mukitlist.core.gathering.constant.GatheringType.*;
import static com.fesi.mukitlist.core.gathering.constant.LocationType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.core.Review;
import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.repository.UserRepository;
import com.fesi.mukitlist.core.repository.gathering.GatheringRepository;
import com.fesi.mukitlist.core.repository.review.ReviewRepository;
import com.fesi.mukitlist.core.repository.usergathering.UserGatheringRepository;
import com.fesi.mukitlist.core.usergathering.UserGathering;
import com.fesi.mukitlist.core.usergathering.UserGatheringId;

@ActiveProfiles("test")
@SpringBootTest
class ParticipationServiceTest {
	private final LocalDateTime TEST_TIME = LocalDateTime.now().plusDays(1);

	@Autowired
	private ParticipationService participationService;

	@Autowired
	private UserGatheringRepository userGatheringRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GatheringRepository gatheringRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	@BeforeEach
	void setUp() {
		// before method
		// 각 테스트 입장에서 봤을 때 : 아예 몰라도 테스트 내용을 이해하는 데에 문제가 없는가?
		// 수정해도 모든 테스트에 영향을 주지 않는가?
		User user = User.builder()
			.email("test@test.com")
			.password("test1234!")
			.nickname("test")
			.createdAt(TEST_TIME)
			.updatedAt(TEST_TIME)
			.build();
		userRepository.save(user);
	}

	@AfterEach
	void tearDown() {
		reviewRepository.deleteAllInBatch();
		userGatheringRepository.deleteAllInBatch();
		gatheringRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("모임에 참여합니다.")
	@Test
	void joinGathering() {
		// given
		User user = createUser("assert@test.com", "assert");
		userRepository.save(user);

		Gathering gathering = createGathering(2, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();
		// when
		Gathering response = participationService.joinGathering(gathering, user, joinedTime);

		// then
		assertThat(response.getParticipantCount()).isEqualTo(1);
	}

	@DisplayName("이미 지난 모임은 참여할 수 없습니다.")
	@Test
	void isPastGathering() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering = createGathering(2, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = TEST_TIME.plusHours(1);

		// when // then
		assertThatThrownBy(() -> participationService.joinGathering(gathering, user, joinedTime)).isInstanceOf(
				AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("PAST_GATHERING", "이미 지난 모임입니다.");
	}

	@DisplayName("취소된 모임은 참여할 수 없습니다.")
	@Test
	void joinCanceledGathering() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering = createGathering(2, 5, TEST_TIME);
		gathering.updateCanceledAt(TEST_TIME.plusHours(1));
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();

		// when // then
		assertThatThrownBy(() -> participationService.joinGathering(gathering, user, joinedTime)).isInstanceOf(
				AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("CANCELED_GATHERING", "취소된 모임입니다.");
	}

	// 로직의 에러를 테스트 코드를 짬으로서 찾음
	@DisplayName("정원을 초과한 모임엔 참여할 수 없습니다.")
	@Test
	void maximumParticipantGathering() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering = createGathering(0, 0, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();

		// when // then
		assertThatThrownBy(() -> participationService.joinGathering(gathering, user, joinedTime)).isInstanceOf(
				AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("MAXIMUM_PARTICIPANTS", "정원 초과 입니다.");
	}

	@DisplayName("이미 참여한 모임엔 참여할 수 없습니다.")
	@Test
	void alreadyJoinedGathering() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering = createGathering(2, 2, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();
		Gathering joinedGathering = participationService.joinGathering(gathering, user, joinedTime);

		// when // then
		assertThatThrownBy(() -> participationService.checkAlreadyJoinedGathering(joinedGathering, user)).isInstanceOf(
				AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("ALREADY_JOINED_GATHERING", "이미 참여한 모임입니다.");
	}

	@DisplayName("모임을 떠납니다.")
	@Test
	void leaveGathering() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering = createGathering(2, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		UserGathering userGathering = UserGathering.of(userGatheringId, TEST_TIME);
		userGatheringRepository.save(userGathering);

		// when
		Gathering response = participationService.leaveGathering(gathering, user, TEST_TIME);

		// then
		assertThat(response.getParticipantCount()).isEqualTo(0);
	}

	@DisplayName("이미 지난 모임은 떠날 수 없습니다.")
	@Test
	void isPastGatheringLeave() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering = createGathering(2, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = TEST_TIME.plusHours(1);

		// when // then
		assertThatThrownBy(() -> participationService.leaveGathering(gathering, user, joinedTime)).isInstanceOf(
				AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("PAST_GATHERING", "이미 지난 모임입니다.");
	}

	//TODO 취소된 모임에 대한 떠남 처리는 어떻게 처리할 지 논의
	@DisplayName("취소된 모임은 떠날 수 없습니다.")
	@Test
	void isCanceledGatheringLeave() {
		// given

		// when

		// then
	}

	@DisplayName("참여하지 않은 모임은 떠날 수 없습니다.")
	@Test
	void notParticipantsLeave() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();
		Gathering gathering = createGathering(2, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		UserGathering userGathering = UserGathering.of(userGatheringId, TEST_TIME);
		userGatheringRepository.save(userGathering);

		participationService.leaveGathering(gathering, user, TEST_TIME);

		//when // then
		assertThatThrownBy(() -> participationService.leaveGathering(gathering, user, TEST_TIME)).isInstanceOf(
				AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("FORBIDDEN", "모임에 참석하지 않았습니다.");
	}

	@DisplayName("이미 떠난 모임은 떠날 수 없습니다.")
	@Test
	void alreadyParticipantsLeave() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();
		Gathering gathering = createGathering(2, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		UserGathering userGathering = UserGathering.of(userGatheringId, TEST_TIME);
		userGatheringRepository.save(userGathering);

		participationService.leaveGathering(gathering, user, TEST_TIME);

		//when // then
		assertThatThrownBy(() -> participationService.checkAlreadyLeavedGathering(gathering, user)).isInstanceOf(
				AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("ALREADY_LEAVED_GATHERING", "이미 참여 취소한 모임입니다.");
	}

	@Test
	@DisplayName("모임 참가자 리스트를 조회합니다.")
	void getParticipantsBy() {
		// given
		User user1 = createUser("user1@test.com", "user1");
		User user2 = createUser("user2@test.com", "user2");
		userRepository.saveAll(List.of(user1, user2));

		Gathering gathering = createGathering(1, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		UserGatheringId userGatheringId1 = UserGatheringId.of(user1, gathering);
		UserGatheringId userGatheringId2 = UserGatheringId.of(user2, gathering);

		userGatheringRepository.saveAll(
			List.of(UserGathering.of(userGatheringId1, TEST_TIME), UserGathering.of(userGatheringId2, TEST_TIME)));

		// when
		List<UserGathering> participants = participationService.getParticipantsBy(gathering);

		// then
		assertThat(participants).hasSize(2)
			.extracting("id")
			.extracting("user")
			.extracting("email")
			.containsExactlyInAnyOrder("user1@test.com", "user2@test.com");
	}

	@Test
	@DisplayName("완료된 참여한 모임을 조회합니다.")
	void getParticipantsWithCompletedFilter() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		LocalDateTime now = LocalDateTime.now();
		Gathering gathering1 = createGathering(1, 5, now.minusDays(1));
		Gathering gathering2 = createGathering(1, 5, now.plusDays(1));
		gatheringRepository.saveAll(List.of(gathering1, gathering2));

		UserGatheringId userGatheringId1 = UserGatheringId.of(user, gathering1);
		UserGatheringId userGatheringId2 = UserGatheringId.of(user, gathering2);

		userGatheringRepository.saveAll(List.of(
			UserGathering.of(userGatheringId1, now.minusDays(2)),
			UserGathering.of(userGatheringId2, now)));
		Pageable pageable = Pageable.unpaged();

		// when
		Page<UserGathering> participants = participationService.getParticipantsWithFilters(user, true, null, pageable);

		// then
		assertThat(participants).hasSize(1)
			.extracting("id")
			.extracting("gathering")
			.extracting("name")
			.containsExactly("성수동 카페");
	}

	// TODO 이게 여기 있는게 맞을까..
	@Test
	@DisplayName("리뷰를 남긴 모임을 조회 합니다.")
	void getParticipantsWithReviewedFilter() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering1 = createGathering(1, 5, TEST_TIME);
		Gathering gathering2 = createGathering(1, 6, TEST_TIME);
		gatheringRepository.saveAll(List.of(gathering1, gathering2));

		Review review = Review.builder()
			.user(user)
			.gathering(gathering1)
			.score(5)
			.comment("좋아요")
			.build();
		reviewRepository.save(review);

		UserGatheringId userGatheringId1 = UserGatheringId.of(user, gathering1);
		UserGatheringId userGatheringId2 = UserGatheringId.of(user, gathering2);
		userGatheringRepository.saveAll(
			List.of(
				UserGathering.of(userGatheringId1, TEST_TIME),
				UserGathering.of(userGatheringId2, TEST_TIME)
			)
		);
		Pageable pageable = Pageable.unpaged();

		// when
		Page<UserGathering> participants = participationService.getParticipantsWithFilters(user, null, true, pageable);

		// then
		assertThat(participants).hasSize(1)
			.extracting("id")
			.extracting("gathering")
			.extracting("capacity")
			.containsExactly(5);
	}

	// TODO 배타적인지 확인
	@Test
	@DisplayName("리뷰를 남긴 완료된 모임을 조회합니다.")
	void getParticipantsWithCompletedAndReviewedFilters() {
		// given
		User user = userRepository.findByEmail("test@test.com").orElseThrow();

		Gathering gathering1 = createGathering(1, 5, TEST_TIME.minusDays(1));
		Gathering gathering2 = createGathering(1, 6, TEST_TIME);
		gatheringRepository.saveAll(List.of(gathering1, gathering2));

		Review review = Review.builder()
			.user(user)
			.gathering(gathering1)
			.score(5)
			.comment("좋아요")
			.build();
		reviewRepository.save(review);

		UserGatheringId userGatheringId1 = UserGatheringId.of(user, gathering1);
		UserGatheringId userGatheringId2 = UserGatheringId.of(user, gathering2);
		userGatheringRepository.saveAll(
			List.of(
				UserGathering.of(userGatheringId1, TEST_TIME.minusDays(2)),
				UserGathering.of(userGatheringId2, TEST_TIME)
			)
		);
		Pageable pageable = Pageable.unpaged();

		// when
		Page<UserGathering> participants = participationService.getParticipantsWithFilters(user, true, true, pageable);

		// then
		assertThat(participants).hasSize(1)
			.extracting("id")
			.extracting("gathering")
			.extracting("name")
			.containsExactly("성수동 카페");

	}
	
	private Gathering createGathering(int openParticipantCount, int capacity, LocalDateTime dateTime) {
		return Gathering.builder()
			.location(SEOUL)
			.type(CAFE)
			.name("성수동 카페")
			.dateTime(dateTime)
			.openParticipantCount(openParticipantCount)
			.capacity(capacity)
			.registrationEnd(dateTime.minusHours(6))
			.address1("성동구")
			.address2("성수동")
			.description("성수동 카페 탐방")
			.createdAt(LocalDateTime.now())
			.createdBy("test")
			.build();
	}

	private User createUser(String email, String nickname) {
		return User.builder()
			.email(email)
			.password("test1234!")
			.nickname(nickname)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}