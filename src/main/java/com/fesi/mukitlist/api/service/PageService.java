package com.fesi.mukitlist.api.service;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fesi.mukitlist.api.exception.AppException;


public class PageService {
	@Bean
	public static Pageable pageableBy(int page, int size, String sort, String direction) {
		checkMinimumSize(size);
		Sort sortOrder = Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction)));
		return PageRequest.of(page, size, sortOrder);
	}

	private static void checkMinimumSize(int size) {
		if (size <= 0) {
			throw new AppException(AT_LEAST_ONE);
		}
	}
}
