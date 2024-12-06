package com.fesi.mukitlist.api.repository;

import java.util.Optional;

import com.fesi.mukitlist.domain.auth.Token;
import com.fesi.mukitlist.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false)\s
      """)
    Token findByUserAndToken(User user, String refreshToken);
}