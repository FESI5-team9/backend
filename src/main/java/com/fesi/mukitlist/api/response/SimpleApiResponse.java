package com.fesi.mukitlist.api.response;

public record SimpleApiResponse(
	String message
)
{
	public static SimpleApiResponse of(String message) {
		return new SimpleApiResponse(message);
	}
}
