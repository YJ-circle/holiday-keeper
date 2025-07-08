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
@RequestMapping("/holiday")
@RequiredArgsConstructor
public class HolidayController {
	private final HolidayService holidayService;

	@GetMapping
	public ResponseEntity getHoliday(
		@PageableDefault(size = 15, sort = "date")  Pageable pageable) {

		return success(holidayService.getAllHoliday(pageable));
	}

	@GetMapping("/country/{country}")
	public ResponseEntity getHolidayByCountry(
		@PathVariable("country") String country,
		@PageableDefault(size = 15, sort = "date") Pageable pageable) {

		return success(holidayService.getAllHolidayByCountry(country, pageable));
	}

	@GetMapping("/year/{year}")
	public ResponseEntity getHolidayByYear(
		@PathVariable("year") String year,
		@PageableDefault(size = 15, sort = "date") Pageable pageable) {

		return success(holidayService.getAllHolidayByYear(year, pageable));
	}

	@GetMapping({"/country/{country}/year/{year}", "/year/{year}/country/{country}"})
	public ResponseEntity getHolidayByCountryAndYear(
		@PathVariable("country") String country,
		@PathVariable("year") String year,
		@PageableDefault(size = 15, sort = "date") Pageable pageable) {

		return success(holidayService.getAllHolidayByCountryAndYear(country, year, pageable));
	}
}
