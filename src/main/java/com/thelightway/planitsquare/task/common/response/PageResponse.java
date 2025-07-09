package com.thelightway.planitsquare.task.common.response;

import java.util.List;

import lombok.Builder;

/**
 * 페이지네이션 응답을 위한 응답 객체입니다.
 */
@Builder
public record PageResponse<T>(
	List<T> content,
	int page,
	int size,
	long totalElements,
	int totalPages
) {
}


