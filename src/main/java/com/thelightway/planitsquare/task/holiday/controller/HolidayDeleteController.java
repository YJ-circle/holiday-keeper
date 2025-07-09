package com.thelightway.planitsquare.task.holiday.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.holiday.controller.dto.HolidayDeleteRequest;
import com.thelightway.planitsquare.task.holiday.dto.HolidayResponse;
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
public class HolidayDeleteController {
	private static final String successMessage = "공휴일 삭제 요청 성공";
	private final HolidayService holidayService;

	@Operation(summary = "공휴일 정보 삭제", description = "특정 연도와 국가의 공휴일 정보를 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "삭제 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@DeleteMapping
	public ResponseEntity deleteHoliday(HolidayDeleteRequest holidayDeleteRequest) {
		List<HolidayResponse> response = holidayService.deleteHoliday(holidayDeleteRequest.countryCode(),
			holidayDeleteRequest.year());

		return success(successMessage, response);
	}
}
