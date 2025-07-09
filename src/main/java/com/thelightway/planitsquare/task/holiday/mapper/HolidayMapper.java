package com.thelightway.planitsquare.task.holiday.mapper;

import java.util.Collections;

import org.springframework.data.domain.Page;

import com.thelightway.planitsquare.task.common.response.PageResponse;
import com.thelightway.planitsquare.task.holiday.dto.HolidayResponse;
import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;
import com.thelightway.planitsquare.task.scraper.holiday.batch.dto.Holiday;

public class HolidayMapper {
	/**
	 * holiday DTO 불변 객체를 Entity 객체로 변환합니다.
	 */
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

	/**
	 * Jpa Repository의 Entity 객체를 불변객체 Response 객체로 타입을 변환합니다.
	 */
	public static HolidayResponse toResponse(HolidayEntity e) {
		return HolidayResponse.builder()
			.date(e.getDate())
			.localName(e.getLocalName())
			.name(e.getName())
			.countryCode(e.getCountryCode())
			.global(e.isGlobal())
			.counties(e.getCounties())
			.types(e.getTypes())
			.build();
	}

	/**
	 * Jpa Repository의 Page 객체를 PageResponse 객체로 변환합니다.
	 * 변환 과정에서 페이지의 content를 불변 List로 바꿉니다.
	 */
	public static PageResponse<HolidayResponse> toPageResponse(Page<HolidayResponse> p) {
		return PageResponse.<HolidayResponse>builder()
			.content(Collections.unmodifiableList(p.getContent()))
			.page(p.getNumber())
			.size(p.getSize())
			.totalElements(p.getTotalElements())
			.totalPages(p.getTotalPages())
			.build();
	}
}
