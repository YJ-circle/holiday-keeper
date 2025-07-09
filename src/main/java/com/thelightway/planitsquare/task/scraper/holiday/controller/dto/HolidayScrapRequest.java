package com.thelightway.planitsquare.task.scraper.holiday.controller.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record HolidayScrapRequest(
	@Schema(
		description = "ISO 국가 코드 목록 (예: [\"KR\",\"US\"])",
		example = "[\"KR\",\"US\"]"
	)
	List<String> countries,

	@Schema(
		description = "연도 (YYYY)",
		example = "2025"
	)
	String year
) {
}
