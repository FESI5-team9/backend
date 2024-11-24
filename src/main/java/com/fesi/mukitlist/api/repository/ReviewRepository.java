package com.fesi.mukitlist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.api.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}