package com.fesi.mukitlist.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fesi.mukitlist.logging.trace.LogTrace;
import com.fesi.mukitlist.logging.trace.TraceStatus;

@Aspect
@Component
public class LogTraceAspect {

	private final LogTrace logTrace;

	public LogTraceAspect(LogTrace logTrace) {
		this.logTrace = logTrace;
	}

	@Around("allControllerMethods()")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		TraceStatus status = null;
		try {
			String message = joinPoint.getSignature().toShortString();
			status = logTrace.begin(message);

			Object result = joinPoint.proceed();

			logTrace.end(status);
			return result;
		} catch (Exception e) {
			logTrace.exception(status, e);
			throw e;
		}
	}

	@Pointcut("execution(* com.fesi.mukitlist.api.controller..*.*(..))")
	public void allControllerMethods() {
	}

}
