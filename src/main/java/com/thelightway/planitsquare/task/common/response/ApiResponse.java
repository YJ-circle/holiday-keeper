package com.thelightway.planitsquare.task.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

/**
 * API 응답을 통일된 형태로 응답하기 위한 클래스입니다.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
	private String errorCode;
	private String message;
	private T data;

	public static ResponseEntity<ApiResponse<Void>> success(String message) {
		ApiResponse<Void> body = ApiResponse.<Void>builder()
			.message(message)
			.build();
		return ResponseEntity.ok(body);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
		ApiResponse<T> body = ApiResponse.<T>builder()
			.message(message)
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