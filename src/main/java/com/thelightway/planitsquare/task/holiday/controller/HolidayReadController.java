package com.thelightway.planitsquare.task.holiday.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.holiday.service.HolidayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayReadController {
	private final HolidayService holidayService;
	private static final String successMessage = "공휴일 조회 요청 성공";

	@GetMapping
	public ResponseEntity getHoliday(
		@PageableDefault(sort = "date")  Pageable pageable) {

		return success(successMessage, holidayService.getAllHoliday(pageable));
	}

	@GetMapping("/country/{country}")
	public ResponseEntity getHolidayByCountry(
		@PathVariable("country") String country,
		@PageableDefault(sort = "date") Pageable pageable) {

		return success(successMessage, holidayService.getAllHolidayByCountry(country, pageable));
	}

	@GetMapping("/year/{year}")
	public ResponseEntity getHolidayByYear(
		@PathVariable("year") String year,
		@PageableDefault(sort = "date") Pageable pageable) {

		return success(successMessage, holidayService.getAllHolidayByYear(year, pageable));
	}

	@GetMapping({"/country/{country}/year/{year}", "/year/{year}/country/{country}"})
	public ResponseEntity getHolidayByCountryAndYear(
		@PathVariable("country") String country,
		@PathVariable("year") String year,
		@PageableDefault(sort = "date") Pageable pageable) {

		return success(successMessage, holidayService.getAllHolidayByCountryAndYear(country, year, pageable));
	}
}
