package com.fesi.mukitlist.api.repository;

import java.util.List;
import java.util.Optional;

import com.fesi.mukitlist.api.domain.Token;
import com.fesi.mukitlist.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    List<Token> findAllValidTokenByUser(User user);
    Optional<Token> findByToken(String token);

}

