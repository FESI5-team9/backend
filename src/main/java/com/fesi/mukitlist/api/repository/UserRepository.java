package com.fesi.mukitlist.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fesi.mukitlist.core.auth.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

	boolean existsUserByEmail(String email);

	boolean existsUserByNickname(String nickname);
}

