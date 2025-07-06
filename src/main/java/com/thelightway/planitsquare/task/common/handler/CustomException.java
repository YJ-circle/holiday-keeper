package com.thelightway.planitsquare.task.common.handler;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {
	private HttpStatus httpStatus;

	public CustomException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public CustomException(HttpStatus httpStatus, String message, Throwable cause) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}
}