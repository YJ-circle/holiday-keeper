package com.thelightway.planitsquare.task.country.repository.entity;

import com.thelightway.planitsquare.task.country.dto.Country;

public class CountyEntityMapper {
	public static CountryEntity toEntity(Country country) {
		return CountryEntity.builder()
			.code(country.code())
			.name(country.name())
			.build();
	}

	public static Country toDto(CountryEntity countryEntity) {
		return Country.builder()
			.code(countryEntity.getCode())
			.name(countryEntity.getName())
			.build();
	}
}
