package com.fesi.mukitlist.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.api.domain.Gathering;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

}