package com.thelightway.planitsquare.task.scraper.exception;

public abstract class RetryableException extends RuntimeException {
	public RetryableException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public RetryableException(String msg) {
		super(msg);
	}
}