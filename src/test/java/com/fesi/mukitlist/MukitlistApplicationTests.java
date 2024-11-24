package com.fesi.mukitlist;

import com.fesi.mukitlist.api.domain.User;
import com.fesi.mukitlist.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MukitlistApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void testCreateUser() {
		// Create a new user
		User user = User.builder()
				.email("testuser@example.com")
				.password("password123")
				.name("Test User")
				.companyName("Test Company")
				.image("default.jpg")
				.build();

		// Save the user to the repository
		User savedUser = userRepository.save(user);
		System.out.println("Saved User: " + savedUser);

		// Assertions
		assertNotNull(savedUser.getId(), "사용자 ID는 저장 후 null이 아니어야 합니다.");
		assertNotNull(savedUser.getCreatedAt(), "CreatedAt은 자동으로 설정되어야 합니다.");
		assertNotNull(savedUser.getUpdatedAt(), "UpdatedAt은 자동으로 설정되어야 합니다.");
		assertNull(savedUser.getDeletedAt(), "DeletedAt은 처음에 null이어야 합니다.");
	}
}
