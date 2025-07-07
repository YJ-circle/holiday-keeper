package com.thelightway.planitsquare.task.scraper.holiday.entity;

import com.thelightway.planitsquare.task.scraper.holiday.dto.Holiday;

public class HolidayEntityMapper {
	public static HolidayEntity toEntity(Holiday holiday) {
		return HolidayEntity.builder()
			.date(holiday.date())
			.localName(holiday.localName())
			.name(holiday.name())
			.countryCode(holiday.countryCode())
			.fixed(holiday.fixed())
			.global(holiday.global())
			.launchYear(holiday.launchYear())
			.counties(holiday.counties())
			.types(holiday.types())
			.build();
	}
}
