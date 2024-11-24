package com.fesi.mukitlist.api.service.request;

import java.time.LocalDateTime;

import com.fesi.mukitlist.api.domain.GatheringType;

import lombok.Builder;

@Builder
public record GatheringServiceCreateRequest(
        GatheringType type,
        String location,
        String name,
        LocalDateTime dateTime,
        int capacity,

        // MultipartFile image
        LocalDateTime registrationEnd
) {

}
