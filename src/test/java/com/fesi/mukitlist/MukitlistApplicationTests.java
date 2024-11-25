package com.fesi.mukitlist;

import com.fesi.mukitlist.api.domain.User;
import com.fesi.mukitlist.api.repository.UserRepository;
import com.fesi.mukitlist.api.controller.dto.request.UserCreateRequest;
import com.fesi.mukitlist.api.service.UserService;
import com.fesi.mukitlist.exception.EmailExistedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MukitlistApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private String uniqueEmail;
	private String name;
	private String password;
	private String companyName;

	@BeforeEach
	void setUp() {
		// Initialize variables with unique values before each test
		uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
		name = "테스트 사용자";
		password = "password123";
		companyName = "테스트 회사";
	}

	@Test
	void testCreateUser() throws EmailExistedException {
		UserCreateRequest userCreateRequest = new UserCreateRequest(
				uniqueEmail, password, name, companyName, null
		);

		userService.registerUser(userCreateRequest.email(), userCreateRequest.name(), userCreateRequest.password(), userCreateRequest.companyName());

		Optional<User> user = userRepository.findByEmail(uniqueEmail);

		assertTrue(user.isPresent(), "User should be present in the database");

		User savedUser = user.get();
		assertNotEquals(password, savedUser.getPassword(), "Password should be hashed");

		assertTrue(passwordEncoder.matches(password, savedUser.getPassword()), "Password should match the hashed password");
	}
}
