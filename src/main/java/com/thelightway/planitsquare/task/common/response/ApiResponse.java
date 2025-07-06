package com.thelightway.planitsquare.task.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
	private String errorCode;
	private String message;
	private T data;

	public static ResponseEntity<ApiResponse<Void>> success() {
		ApiResponse<Void> body = ApiResponse.<Void>builder()
			.build();
		return ResponseEntity.ok(body);
	}

	public static ResponseEntity<ApiResponse<Void>> success(String message) {
		ApiResponse<Void> body = ApiResponse.<Void>builder()
			.message(message)
			.build();
		return ResponseEntity.ok(body);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
		ApiResponse<T> body = ApiResponse.<T>builder()
			.data(data)
			.build();
		return ResponseEntity.ok(body);
	}

	public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
		ApiResponse<T> body = ApiResponse.<T>builder()
			.message(message)
			.build();
		return ResponseEntity.status(status).body(body);
	}

}