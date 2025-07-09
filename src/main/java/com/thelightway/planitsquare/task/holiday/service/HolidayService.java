package com.thelightway.planitsquare.task.holiday.service;

import static com.thelightway.planitsquare.task.common.utils.DateUtils.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thelightway.planitsquare.task.common.response.PageResponse;
import com.thelightway.planitsquare.task.holiday.dto.HolidayResponse;
import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;
import com.thelightway.planitsquare.task.holiday.mapper.HolidayMapper;
import com.thelightway.planitsquare.task.holiday.repository.HolidayJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HolidayService {
	private final HolidayJpaRepository holidayJpaRepository;

	public Page<HolidayResponse> getAllHoliday(Pageable pageable) {
		return holidayJpaRepository.findAllByActive(true, pageable)
			.map(HolidayMapper::toResponse);
	}

	public PageResponse<HolidayResponse> getAllHolidayByCountry(String country, Pageable pageable) {
		Page<HolidayResponse> holidayPage = holidayJpaRepository.findByCountryCodeAndActive(country, true, pageable)
			.map(HolidayMapper::toResponse);
		return HolidayMapper.toPageResponse(holidayPage);
	}

	public PageResponse<HolidayResponse> getAllHolidayByYear(String requestYear, Pageable pageable) {
		int year = Integer.parseInt(requestYear);
		Page<HolidayResponse> holidayPage = holidayJpaRepository.findByDateBetweenAndActive(
			firstDayByYear(year), lastDayByYear(year), true, pageable
		).map(HolidayMapper::toResponse);

		return HolidayMapper.toPageResponse(holidayPage);
	}

	public PageResponse<HolidayResponse> getAllHolidayByCountryAndYear(String country, String requestYear,
		Pageable pageable) {
		int year = Integer.parseInt(requestYear);
		Page<HolidayResponse> holidayPage = holidayJpaRepository.findByCountryCodeAndDateBetweenAndActive(
			country, firstDayByYear(year), lastDayByYear(year), true, pageable
		).map(HolidayMapper::toResponse);
		return HolidayMapper.toPageResponse(holidayPage);
	}

	public List<HolidayResponse> deleteHoliday(String countryCode, String requestYear) {
		int year = Integer.parseInt(requestYear);
		List<HolidayEntity> toDeleteEntities = holidayJpaRepository.findAllByCountryCodeAndDateBetweenAndActive(
			countryCode, firstDayByYear(year), lastDayByYear(year), true);

		toDeleteEntities.forEach((e) -> e.changeActiveStatus(false));

		return holidayJpaRepository.saveAll(toDeleteEntities)
			.stream()
			.map(HolidayMapper::toResponse)
			.toList();
	}
}
