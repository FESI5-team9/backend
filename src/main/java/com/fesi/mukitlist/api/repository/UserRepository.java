package com.fesi.mukitlist.api.repository;

import com.fesi.mukitlist.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String mail);
}
