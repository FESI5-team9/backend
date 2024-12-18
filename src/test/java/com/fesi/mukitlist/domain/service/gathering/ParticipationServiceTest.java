package com.fesi.mukitlist.domain.service.gathering;

import static com.fesi.mukitlist.core.gathering.constant.GatheringType.*;
import static com.fesi.mukitlist.core.gathering.constant.LocationType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.repository.UserRepository;
import com.fesi.mukitlist.core.repository.gathering.GatheringRepository;
import com.fesi.mukitlist.core.repository.usergathering.UserGatheringRepository;

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
		userGatheringRepository.deleteAllInBatch();
		gatheringRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("모임에 참여한다.")
	@Test
	void joinGathering() {
		// given
		User user = createUser("assert@test.com", "assert");
		userRepository.save(user);

		Gathering gathering = createGathering(2, 0, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();
		// when
		Gathering response = participationService.joinGathering(gathering, user, joinedTime);

		// then
		assertThat(response.getParticipantCount()).isEqualTo(1);
	}

	@DisplayName("취소된 모임에는 참여할 수 없다")
	@Test
	void joinCanceledGathering() {
		// given
		User user = userRepository.findById(1L).orElseThrow();

		Gathering gathering = createGathering(2, 0, 5, TEST_TIME);
		gathering.updateCanceledAt(TEST_TIME.plusHours(1));
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();

		// when // then
		assertThatThrownBy(() -> participationService.joinGathering(gathering, user, joinedTime))
			.isInstanceOf(AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("CANCELED_GATHERING", "취소된 모임입니다.");
	}

	// 로직의 에러를 테스트 코드를 짬으로서 찾음
	@DisplayName("정원을 초과한 모임엔 참여할 수 없다.")
	@Test
	void maximumParticipantGathering() {
		// given
		User user = userRepository.findById(1L).orElseThrow();

		Gathering gathering = createGathering(2, 5, 5, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();

		// when // then
		assertThatThrownBy(() -> participationService.joinGathering(gathering, user, joinedTime))
			.isInstanceOf(AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("MAXIMUM_PARTICIPANTS", "정원 초과 입니다.");
	}

	@DisplayName("이미 참여한 모임엔 참여할 수 없습니다.")
	@Test
	void alreadyJoinedGathering() {
		// given
		User user = userRepository.findById(1L).orElseThrow();

		Gathering gathering = createGathering(2, 0, 2, TEST_TIME);
		gatheringRepository.save(gathering);

		LocalDateTime joinedTime = LocalDateTime.now();
		Gathering joinedGathering = participationService.joinGathering(gathering, user, joinedTime);
		;
		// when // then
		assertThatThrownBy(() -> participationService.checkAlreadyJoinedGathering(joinedGathering, user))
			.isInstanceOf(AppException.class)
			.extracting("exceptionCode")
			.extracting("code", "message")
			.containsExactly("ALREADY_JOINED_GATHERING", "이미 참여한 모임입니다.");
	}

	@Test
	void leaveGathering() {
	}

	@Test
	void getParticipantsBy() {
	}

	@Test
	void testGetParticipantsBy() {
	}

	@Test
	void testGetParticipantsBy1() {
	}

	@Test
	void getParticipantsWithFilters() {
	}

	private Gathering createGathering(int openParticipantCount, int participantCount, int capacity,
		LocalDateTime dateTime) {
		return Gathering.builder()
			.location(SEOUL)
			.type(CAFE)
			.name("성수동 카페")
			.dateTime(dateTime)
			.openParticipantCount(openParticipantCount)
			.participantCount(participantCount)
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