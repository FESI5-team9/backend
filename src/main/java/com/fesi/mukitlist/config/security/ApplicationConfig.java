package com.fesi.mukitlist.config.security;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.core.auth.PrincipalDetails;
import com.fesi.mukitlist.core.auth.application.User;
import com.fesi.mukitlist.core.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final UserRepository userRepository;

	// TODO security가 제안하는 방식으로 변경
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new AppException(NOT_FOUND_USER));
			return new PrincipalDetails(user);
		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
