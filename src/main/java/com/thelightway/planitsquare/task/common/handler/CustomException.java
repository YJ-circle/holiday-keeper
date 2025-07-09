package com.thelightway.planitsquare.task.common.handler;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * API 요청의 오류를 전역으로 처리하기 위한 Custom Exception 입니다.
 */
@Getter
public abstract class CustomException extends RuntimeException {
	private final HttpStatus httpStatus;

	public CustomException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public CustomException(HttpStatus httpStatus, String message, Throwable cause) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}
}