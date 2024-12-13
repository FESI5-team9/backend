package com.fesi.mukitlist.domain.service.auth;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.core.auth.PrincipalDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${security.jwt.secret-key}")
	private String secretKey;
	@Value("${security.jwt.refresh-secret-key}")
	private String refreshSecretKey;
	@Value("${security.jwt.expiration}")
	private long accessExpiration;
	@Value("${security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateAccessToken(PrincipalDetails principalDetails) {
		return buildToken(Map.of("access-token",true), principalDetails, accessExpiration);
	}

	public String generateRefreshToken(PrincipalDetails principalDetails) {
		return buildToken(Map.of("refresh-token", true), principalDetails, refreshExpiration);
	}

	private String buildToken(Map<String, Object> extraClaims, PrincipalDetails principalDetails, long expiration) {
		return Jwts.builder()
			.setClaims(extraClaims)
			.setSubject(principalDetails.getUsername())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(getSignInKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public boolean isRefreshTokenValid(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(getRefreshSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
			Boolean isRefresh = claims.get("refresh-token", Boolean.class);
			return Boolean.TRUE.equals(isRefresh) && !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}

	private SecretKey getSignInKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
	}
	private Key getRefreshSignInKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) throws AppException {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			throw new AppException(TOKEN_EXPIRED);
		} catch (JwtValidationException e) {
			throw new AppException(NOT_VALID_TOKEN);
		}
	}
}

