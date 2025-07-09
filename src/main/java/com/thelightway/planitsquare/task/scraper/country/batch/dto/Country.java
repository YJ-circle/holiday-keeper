package com.thelightway.planitsquare.task.scraper.country.batch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record Country (
	@JsonProperty("countryCode")
	String code,
	String name
){}
