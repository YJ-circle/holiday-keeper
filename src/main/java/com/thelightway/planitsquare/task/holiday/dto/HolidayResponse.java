package com.thelightway.planitsquare.task.holiday.dto;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HolidayResponse(
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
	String localName,
	String name,
	String countryCode,
	boolean global,
	Set<String> counties,
	Set<String> types
) {
}
