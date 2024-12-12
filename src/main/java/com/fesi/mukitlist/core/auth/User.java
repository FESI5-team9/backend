package com.fesi.mukitlist.core.auth;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fesi.mukitlist.api.controller.auth.oauth.kakao.request.KakaoServiceCreateRequest;
import com.fesi.mukitlist.core.auth.constant.UserType;
import com.fesi.mukitlist.domain.service.auth.request.UserServiceCreateRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column
	private String password;

	@Column(nullable = false)
	private String nickname;

	private String image;

	private String provider;

	private String providerId;

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
	private User(String email, String password, String nickname, String image, String provider, String providerId,
		UserType userType, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.image = image;
		this.provider = provider;
		this.providerId = providerId;
		this.userType = userType;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}

	public static User create(UserServiceCreateRequest request, String password) {
		return User.builder()
			.email(request.email())
			.password(password)
			.nickname(request.nickname())
			.build();
	}

	public static User createOAuth2User(KakaoServiceCreateRequest request) {
		return User.builder()
			.email(request.email())
			.nickname(request.nickname())
			.userType(request.userType())
			.provider(request.userType().getProviderName())
			.providerId(request.providerId())
			.build();
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateImage(String storedName) {
		this.image = storedName;
	}
}