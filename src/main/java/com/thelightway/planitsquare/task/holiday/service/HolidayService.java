package com.thelightway.planitsquare.task.holiday.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thelightway.planitsquare.task.common.response.PageResponse;
import com.thelightway.planitsquare.task.holiday.mapper.HolidayEntityMapper;
import com.thelightway.planitsquare.task.holiday.repository.HolidayJpaRepository;
import com.thelightway.planitsquare.task.scraper.holiday.dto.HolidayResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HolidayService {
	private final HolidayJpaRepository holidayJpaRepository;

	public Page<HolidayResponse> getAllHoliday(Pageable pageable) {
		return holidayJpaRepository.findAllByActive(true, pageable)
			.map(HolidayEntityMapper::toResponse);
	}

	public PageResponse<HolidayResponse> getAllHolidayByCountry(String country, Pageable pageable) {
		Page<HolidayResponse> holidayPage = holidayJpaRepository.findByCountryCodeAndActive(country, true, pageable)
			.map(HolidayEntityMapper::toResponse);
		return HolidayEntityMapper.toPageResponse(holidayPage);
	}

	public PageResponse<HolidayResponse> getAllHolidayByYear(String requestYear, Pageable pageable) {
		int year = Integer.parseInt(requestYear);
		Page<HolidayResponse> holidayPage = holidayJpaRepository.findByDateBetweenAndActive(
			firstDate(year), lastDate(year), true, pageable
		).map(HolidayEntityMapper::toResponse);

		return HolidayEntityMapper.toPageResponse(holidayPage);
	}

	public PageResponse<HolidayResponse> getAllHolidayByCountryAndYear(String country, String requestYear,
		Pageable pageable) {
		int year = Integer.parseInt(requestYear);
		Page<HolidayResponse> holidayPage = holidayJpaRepository.findByCountryCodeAndActiveAndDateBetween(
			country, true, firstDate(year), lastDate(year), pageable
		).map(HolidayEntityMapper::toResponse);
		return HolidayEntityMapper.toPageResponse(holidayPage);
	}

	private LocalDate firstDate(int year) {
		return LocalDate.of(year, 1, 1);
	}

	private LocalDate lastDate(int year) {
		return LocalDate.of(year, 12, 31);
	}
}
