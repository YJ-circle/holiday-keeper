package com.thelightway.planitsquare.task.country.dto;

import lombok.Builder;

@Builder
public record CountryResponse(
	String code,
	String name
) {}
