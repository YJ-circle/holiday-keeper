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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Holiday", description = "공휴일 API")
@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayReadController {
	private static final String successMessage = "공휴일 조회 요청 성공";
	private final HolidayService holidayService;

	@Operation(summary = "전체 공휴일 조회", description = "저장된 모든 공휴일 정보를 페이지로 나눠 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping
	public ResponseEntity getHoliday(
		@PageableDefault(sort = "date") Pageable pageable) {

		return success(successMessage, holidayService.getAllHoliday(pageable));
	}

	@Operation(summary = "국가별 공휴일 조회", description = "특정 국가의 공휴일 정보를 페이지로 나눠 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 국가 코드"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping("/country/{country}")
	public ResponseEntity getHolidayByCountry(
		@PathVariable("country") String country,
		@PageableDefault(sort = "date") Pageable pageable) {

		return success(successMessage, holidayService.getAllHolidayByCountry(country, pageable));
	}

	@Operation(summary = "연도별 공휴일 조회", description = "특정 연도의 공휴일 정보를 페이지로 나눠 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 연도 형식"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping("/year/{year}")
	public ResponseEntity getHolidayByYear(
		@PathVariable("year") String year,
		@PageableDefault(sort = "date") Pageable pageable) {

		return success(successMessage, holidayService.getAllHolidayByYear(year, pageable));
	}

	@Operation(summary = "국가 및 연도별 공휴일 조회", description = "특정 국가와 연도의 공휴일 정보를 페이지로 나눠 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping({"/country/{country}/year/{year}", "/year/{year}/country/{country}"})
	public ResponseEntity getHolidayByCountryAndYear(
		@PathVariable("country") String country,
		@PathVariable("year") String year,
		@PageableDefault(sort = "date") Pageable pageable) {

		return success(successMessage, holidayService.getAllHolidayByCountryAndYear(country, year, pageable));
	}
}
