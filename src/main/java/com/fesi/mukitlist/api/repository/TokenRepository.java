package com.fesi.mukitlist.api.repository;

import com.fesi.mukitlist.core.auth.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
      select t from Token t inner join User u
      on t.user.id = u.id
      where u.id = :userId and t.token = :refreshToken and t.expired = false
      """)
    Token findByUserAndToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

}