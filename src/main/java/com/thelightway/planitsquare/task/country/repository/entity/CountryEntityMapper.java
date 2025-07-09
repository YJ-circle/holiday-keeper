package com.thelightway.planitsquare.task.country.repository.entity;

import com.thelightway.planitsquare.task.country.dto.CountryResponse;
import com.thelightway.planitsquare.task.scraper.country.batch.dto.Country;

public class CountryEntityMapper {
	public static CountryEntity toEntity(Country country) {
		return CountryEntity.builder()
			.code(country.code())
			.name(country.name())
			.build();
	}

	public static CountryResponse toResponse(CountryEntity countryEntity) {
		return CountryResponse.builder()
			.code(countryEntity.getCode())
			.name(countryEntity.getName())
			.build();
	}
}
