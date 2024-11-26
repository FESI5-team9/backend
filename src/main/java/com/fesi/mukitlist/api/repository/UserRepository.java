package com.fesi.mukitlist.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}