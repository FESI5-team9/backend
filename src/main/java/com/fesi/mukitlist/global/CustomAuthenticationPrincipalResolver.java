package com.fesi.mukitlist.global;

import static com.fesi.mukitlist.api.exception.ExceptionCode.*;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fesi.mukitlist.api.exception.AppException;
import com.fesi.mukitlist.domain.auth.User;

public class CustomAuthenticationPrincipalResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Authorize.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAuthenticatedUser = isAuthenticatedUser(authentication);

		if (parameter.getParameterAnnotation(Authorize.class).required() && !isAuthenticatedUser) {
			throw new AppException(LOGIN_REQUIRED);
		}

		return isAuthenticatedUser ? ((User) authentication.getPrincipal()) : null;
	}

	private boolean isAuthenticatedUser(Authentication authentication) {
		return authentication != null
			&& authentication.isAuthenticated()
			&& authentication.getPrincipal() instanceof User;
	}
}
