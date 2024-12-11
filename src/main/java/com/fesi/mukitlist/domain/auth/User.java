package com.fesi.mukitlist.domain.auth;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.service.auth.request.UserServiceCreateRequest;

import com.fesi.mukitlist.api.service.oauth.request.KakaoUserCreateRequest;
import com.fesi.mukitlist.api.service.oauth.response.SocialUserResponse;
import com.fesi.mukitlist.domain.auth.constant.UserType;
import jakarta.persistence.*;
import lombok.Builder;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;

	private String image;

	@Column(columnDefinition = "ENUM('KAKAO', 'GOOGLE', 'NORMAL') DEFAULT 'NORMAL'")
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	@Builder
	private User(String email, String password, String nickname, String image, UserType userType, LocalDateTime createdAt,
				 LocalDateTime updatedAt, LocalDateTime deletedAt) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.image = image;
		this.userType = userType;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}

	public static User of(UserServiceCreateRequest request, String password) {
		return User.builder()
				.email(request.email())
				.password(password)
				.nickname(request.nickname())
				.build();
	}

	public static User of(KakaoUserCreateRequest request) {
		return User.builder()
				.email(request.email())
				.password(request.password())
				.nickname(request.nickname())
				.userType(request.userType())
				.build();
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateImage(String storedName) {
		this.image = storedName;
	}
}