package com.thelightway.planitsquare.task.common.uribuilder.exception;

import org.springframework.http.HttpStatus;

import com.thelightway.planitsquare.task.common.handler.CustomException;

public class UriBuilderException extends CustomException {
	public UriBuilderException(HttpStatus httpStatus, String message) {
		super(httpStatus, message);
	}
}
