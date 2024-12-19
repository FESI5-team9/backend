package com.fesi.mukitlist.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.core.gathering.Gathering;
import com.fesi.mukitlist.core.gathering.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

	List<Keyword> findAllByGathering(Gathering gathering);
}