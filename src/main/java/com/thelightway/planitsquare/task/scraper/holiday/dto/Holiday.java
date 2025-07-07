package com.thelightway.planitsquare.task.scraper.holiday.dto;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record Holiday(
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
	String localName,
	String name,
	String countryCode,
	boolean fixed,
	@JsonProperty("global") boolean global,
	Set<String> counties,
	Integer launchYear,
	Set<String> types
) {}
