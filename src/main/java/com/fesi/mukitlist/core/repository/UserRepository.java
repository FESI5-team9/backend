package com.fesi.mukitlist.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fesi.mukitlist.core.auth.application.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findById(Long id);

	Optional<User> findByEmail(String email);

	boolean existsUserByEmail(String email);

	boolean existsUserByNickname(String nickname);
}

