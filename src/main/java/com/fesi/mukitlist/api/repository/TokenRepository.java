package com.fesi.mukitlist.api.repository;

import java.util.List;
import java.util.Optional;

import com.fesi.mukitlist.domain.auth.Token;
import com.fesi.mukitlist.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    List<Token> findAllValidTokenByUser(User user);
    Optional<Token> findByToken(String token);
    Optional<Token> findByUser(User user);
}