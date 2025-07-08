package com.thelightway.planitsquare.task.holiday.mapper;

import org.springframework.data.domain.Page;

import com.thelightway.planitsquare.task.common.response.PageResponse;
import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;
import com.thelightway.planitsquare.task.scraper.holiday.dto.Holiday;
import com.thelightway.planitsquare.task.scraper.holiday.dto.HolidayResponse;

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

	public static HolidayResponse toResponse(HolidayEntity e) {
		return HolidayResponse.builder()
			.date(e.getDate())
			.localName(e.getLocalName())
			.name(e.getName())
			.global(e.isGlobal())
			.counties(e.getCounties())
			.types(e.getTypes())
			.build();
	}

	public static PageResponse<HolidayResponse> toPageResponse(Page<HolidayResponse> ph) {
		return PageResponse.<HolidayResponse>builder()
			.content(ph.getContent())
			.page(ph.getNumber())
			.size(ph.getSize())
			.totalElements(ph.getTotalElements())
			.totalPages(ph.getTotalPages())
			.build();
	}
}
