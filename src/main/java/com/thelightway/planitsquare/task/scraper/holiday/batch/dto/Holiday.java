package com.thelightway.planitsquare.task.scraper.holiday.batch.dto;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record Holiday(
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
	String localName,
	String name,
	String countryCode,
	boolean fixed,
	boolean global,
	Set<String> counties,
	Integer launchYear,
	Set<String> types
) {
}
