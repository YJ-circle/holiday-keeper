package com.thelightway.planitsquare.task.scraper.exception;

public class EmptyApiResponseException extends RetryableException{
	public EmptyApiResponseException(String msg) {
		super(msg);
	}

	public EmptyApiResponseException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
