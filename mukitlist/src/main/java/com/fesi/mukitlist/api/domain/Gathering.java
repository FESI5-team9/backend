package com.fesi.mukitlist.api.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Gathering {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private LocalDateTime dateTime;

	private LocalDateTime registrationEnd;

	private String location;

	private int participantCount;

	private int capacity;

	private String image;

	@CreatedBy
	private String createdBy;

	private LocalDateTime canceledAt;

	// User
	// participants
	// Review
}
