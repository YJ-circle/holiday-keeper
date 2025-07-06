package com.thelightway.planitsquare.task.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thelightway.planitsquare.task.common.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
		return buildErrorResponse(
			ex.getHttpStatus(),
			ex.getMessage(),
			ex
		);
	}

	private ResponseEntity<ApiResponse<Void>> buildErrorResponse(HttpStatus status, String message, Throwable ex) {
		log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
		return ApiResponse.error(status, message);
	}

}