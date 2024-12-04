package com.fesi.mukitlist.api.repository;

import static com.fesi.mukitlist.domain.gathering.constant.GatheringType.*;
import static com.fesi.mukitlist.domain.gathering.constant.LocationType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.fesi.mukitlist.api.repository.gathering.GatheringRepository;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;

@ActiveProfiles("test")
@DataJpaTest
class KeywordRepositoryTest {

	@Autowired
	private KeywordRepository keywordRepository;

	@Autowired
	private GatheringRepository gatheringRepository;

	@DisplayName("모임에 맞는 키워드를 가져온다")
	@Test
	void findAllByGathering() {
		// given
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
			.createdBy("test")
			.build();

		Gathering savedGathering = gatheringRepository.save(gathering);

		Keyword keyword1 = Keyword.of("성수", savedGathering);
		Keyword keyword2 = Keyword.of("카페", savedGathering);
		keywordRepository.saveAll(List.of(keyword1, keyword2));

		// when
		List<Keyword> keywords = keywordRepository.findAllByGathering(savedGathering);

		// then
		assertThat(keywords).hasSize(2)
			.extracting("keyword", "gathering")
			.containsExactlyInAnyOrder(
				tuple("성수", savedGathering),
				tuple("카페", savedGathering)
			);
	}
}