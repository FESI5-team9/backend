package com.fesi.mukitlist.api.service.auth;

import com.fesi.mukitlist.domain.auth.PrincipalDetails;
import com.fesi.mukitlist.domain.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	@Value("${security.jwt.secret-key}")
	private String secretKey;
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

	public String generateToken(PrincipalDetails principalDetails) {
		return generateToken(new HashMap<>(), principalDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, PrincipalDetails principalDetails) {
		return buildToken(extraClaims, principalDetails, accessExpiration);
	}

	public String generateRefreshToken(PrincipalDetails principalDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("refresh-token", true);
		return buildToken(claims, principalDetails, refreshExpiration);
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

	public boolean isTokenValid(String token, PrincipalDetails principalDetails) {
		final String username = extractUsername(token);
		return (username.equals(principalDetails.getUsername())) && !isTokenExpired(token);
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

    private Key getRefreshSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSignInKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}

