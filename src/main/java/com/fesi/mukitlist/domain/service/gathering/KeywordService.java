package com.fesi.mukitlist.domain.service.gathering;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.Keyword;
import com.fesi.mukitlist.core.repository.KeywordRepository;

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

	public void updateKeywords(List<String> newKeywordValues, Gathering gathering) {
		if (newKeywordValues == null) {
			return;
		}

		List<Keyword> existingKeywords = keywordRepository.findAllByGathering(gathering);

		List<Keyword> keywordsToDelete = existingKeywords.stream()
			.filter(existing -> !newKeywordValues.contains(existing.getKeyword()))
			.collect(Collectors.toList());
		keywordRepository.deleteAll(keywordsToDelete);

		List<Keyword> keywordsToAdd = newKeywordValues.stream()
			.filter(newValue -> existingKeywords.stream()
				.noneMatch(existing -> existing.getKeyword().equals(newValue)))
			.map(newValue -> Keyword.of(newValue, gathering))
			.collect(Collectors.toList());
		keywordRepository.saveAll(keywordsToAdd);
	}
}
