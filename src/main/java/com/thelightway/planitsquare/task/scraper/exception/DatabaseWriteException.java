package com.thelightway.planitsquare.task.scraper.exception;

public class DatabaseWriteException extends RetryableException{
	public DatabaseWriteException(String msg) {
		super(msg);
	}

	public DatabaseWriteException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
