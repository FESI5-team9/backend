package com.fesi.mukitlist.api.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fesi.mukitlist.api.service.request.GatheringServiceCreateRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Gathering {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private GatheringType type;

	private String name;

	@Column(nullable = false)
	private LocalDateTime dateTime;

	@Column(nullable = false)
	private LocalDateTime registrationEnd;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private int participantCount = 0;

	@Column(nullable = false)
	private int capacity;

	@CreatedBy
	private String createdBy;

	private LocalDateTime canceledAt;

	@Builder
	private Gathering(GatheringType type, String name, LocalDateTime dateTime, LocalDateTime registrationEnd,
		String location, int participantCount, int capacity, String createdBy, LocalDateTime canceledAt) {
		this.type = type;
		this.name = name;
		this.dateTime = dateTime;
		this.registrationEnd = registrationEnd;
		this.location = location;
		this.participantCount = participantCount;
		this.capacity = capacity;
		this.createdBy = createdBy;
		this.canceledAt = canceledAt;
	}

	public static Gathering create(GatheringServiceCreateRequest request) {
		return Gathering.builder()
			.location(request.location())
			.type(request.type())
			.name(request.name())
			.dateTime(request.dateTime())
			.capacity(request.capacity())
			.registrationEnd(request.registrationEnd())
			.createdBy("test")
			.build();
	}

	public void updateCanceledAt(LocalDateTime canceledTime) {
		this.canceledAt = canceledTime;
	}

	public void joinParticipant() {
		this.participantCount++;
	}

	public void leaveParticipant() {
		this.participantCount--;
	}

	// User
	// participants
	// Review
}
