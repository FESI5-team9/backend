package com.fesi.mukitlist.config.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fesi.mukitlist.core.auth.PrincipalDetails;

public class CustomAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof PrincipalDetails) {
			// UserDetails의 구현체에서 닉네임을 가져온다고 가정
			return Optional.of(((PrincipalDetails)principal).getName());
		} else {
			return Optional.of(principal.toString());
		}
	}
}
