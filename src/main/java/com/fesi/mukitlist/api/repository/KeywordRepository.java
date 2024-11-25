package com.fesi.mukitlist.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.api.domain.Gathering;
import com.fesi.mukitlist.api.domain.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

	List<Keyword> findAllByGathering(Gathering gathering);
}