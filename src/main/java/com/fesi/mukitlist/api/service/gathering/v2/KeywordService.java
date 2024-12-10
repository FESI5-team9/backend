package com.fesi.mukitlist.api.service.gathering.v2;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.api.repository.KeywordRepository;
import com.fesi.mukitlist.domain.gathering.Gathering;
import com.fesi.mukitlist.domain.gathering.Keyword;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class KeywordService {
	private final KeywordRepository keywordRepository;

	public List<Keyword> findByGathering(Gathering gathering) {
		return keywordRepository.findAllByGathering(gathering);
	}

	public List<Keyword> saveKeywords(List<String> keyword, Gathering gathering) {
		List<Keyword> keywords = keyword.stream()
			.map(k -> Keyword.of(k, gathering))
			.toList();
		return keywordRepository.saveAll(keywords);
	}
}