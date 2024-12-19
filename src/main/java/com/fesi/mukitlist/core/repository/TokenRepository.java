package com.fesi.mukitlist.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fesi.mukitlist.core.auth.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

	@Query(value = """
		select t from Token t inner join User u
		on t.user.id = u.id
		where u.id = :userId and t.token = :refreshToken and t.expired = false
		""")
	Token findByUserAndToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

	boolean existsTokenByUserId(Long userId);

	Token findFirstByUserId(Long id);

	Token findFirstByToken(String token);
}
