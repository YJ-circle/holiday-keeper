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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class HolidayDeleteController {
	private static final String successMessage = "공휴일 삭제 요청 성공";
	private final HolidayService holidayService;

	@DeleteMapping
	public ResponseEntity deleteHoliday(HolidayDeleteRequest holidayDeleteRequest) {
		List<HolidayResponse> response = holidayService.deleteHoliday(holidayDeleteRequest.countryCode(),
			holidayDeleteRequest.year());

		return success(successMessage, response);
	}
}
