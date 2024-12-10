package com.fesi.mukitlist.global.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class GenericMapper {

	public static <T, R> List<R> mapToList(List<T> source, Function<T, R> mapper) {
		return source.stream()
			.map(mapper)
			.collect(Collectors.toList());
	}

	public static <T, R> Page<R> mapToPage(Page<T> source, Function<T, R> mapper) {
		List<R> content = source.getContent()
			.stream()
			.map(mapper)
			.toList();
		return new PageImpl<>(content, source.getPageable(), source.getTotalElements());
	}
}
