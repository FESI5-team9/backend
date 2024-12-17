package com.fesi.mukitlist.api.repository;

import static com.fesi.mukitlist.core.gathering.constant.GatheringType.*;
import static com.fesi.mukitlist.core.gathering.constant.LocationType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.repository.UserRepository;
import com.fesi.mukitlist.core.repository.gathering.GatheringRepository;
import com.fesi.mukitlist.core.repository.usergathering.UserGatheringRepository;
import com.fesi.mukitlist.core.usergathering.UserGathering;
import com.fesi.mukitlist.core.usergathering.UserGatheringId;

@ActiveProfiles("test")
@DataJpaTest
class UserGatheringRepositoryTest {
	@Autowired
	private UserGatheringRepository userGatheringRepository;
	@Autowired
	private GatheringRepository gatheringRepository;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		User user = User.builder()
			.email("test@test.com")
			.password("test1234!")
			.nickname("test")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.deletedAt(null)
			.build();
		userRepository.save(user);

		Gathering gathering = Gathering.builder()
			.location(SEOUL)
			.type(CAFE)
			.name("성수동 카페")
			.dateTime(LocalDateTime.now())
			.capacity(5)
			.registrationEnd(LocalDateTime.now())
			.address1("성동구")
			.address2("성수동")
			.description("성수동 카페 탐방")
			.createdBy(user.getNickname())
			.user(user)
			.build();
		gatheringRepository.save(gathering);

		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		UserGathering userGathering = UserGathering.of(userGatheringId, LocalDateTime.now());
		userGatheringRepository.save(userGathering);
	}

	@AfterEach
	void tearDown() {
		userGatheringRepository.deleteAllInBatch();
		gatheringRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("로그인된 사용자가 참석한 모임 데이터를 가져온다.")
	@Test
	void findWithFilters() {
		// given
		Gathering gathering = Gathering.builder()
			.location(SEOUL)
			.type(CAFE)
			.name("성수동 카페1")
			.dateTime(LocalDateTime.now())
			.capacity(5)
			.registrationEnd(LocalDateTime.now())
			.address1("성동구")
			.address2("성수동")
			.description("성수동 카페 탐방")
			.createdBy("test")
			.build();
		gatheringRepository.save(gathering);

		User user = userRepository.findById(1L).orElseThrow();
		UserGatheringId userGatheringId = UserGatheringId.of(user, gathering);
		UserGathering userGathering = UserGathering.of(userGatheringId, LocalDateTime.now());
		userGatheringRepository.save(userGathering);

		Pageable pageable = PageRequest.of(0, 10);
		// when
		Page<UserGathering> gatherings = userGatheringRepository.findWithFilters(user, null, null,
			pageable);
		// then
		assertThat(gatherings.getContent()).isNotEmpty().hasSize(2)
			.extracting(
				g -> g.getId().getGathering().getName(),
				g -> g.getId().getUser().getNickname()
			).containsExactlyInAnyOrder(
				tuple("성수동 카페", "name"),
				tuple("성수동 카페1", "name")
			);
	}

	@DisplayName("모임에 참가한 참여자를 가져온다.")
	@Test
	void findByIdGathering() {
		// given
		Gathering gathering = gatheringRepository.findById(1L).orElseThrow();
		Pageable pageable = PageRequest.of(0, 10);

		// when
		Page<UserGathering> participants = userGatheringRepository.findByIdGathering(gathering, pageable);

		// then
		assertThat(participants.getContent()).hasSize(1)
			.extracting(
				ug -> ug.getId().getGathering().getName(),
				ug -> ug.getId().getUser().getNickname()
			)
			.containsExactlyInAnyOrder(
				tuple("성수동 카페", "name")
			);
	}
}