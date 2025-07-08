package com.thelightway.planitsquare.task.scraper.holiday.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public record HolidayScrapRequest(
	@Schema(
		description = "ISO 국가 코드 목록 (예: [\"KR\",\"US\"])",
		example = "[\"KR\",\"US\"]",
		required = true
	)
	List<String> countries,

	@Schema(
		description = "연도 (YYYY)",
		example = "2025",
		required = true
	)
	String year
) {}
