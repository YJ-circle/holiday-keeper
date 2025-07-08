package com.thelightway.planitsquare.task.scraper.holiday.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.scraper.AbstractScraperController;
import com.thelightway.planitsquare.task.scraper.holiday.controller.dto.HolidayScrapRequest;
import com.thelightway.planitsquare.task.scraper.holiday.service.HolidayScraperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Holiday", description = "공휴일 API")
@RestController
@RequiredArgsConstructor
public class HolidayScraperCtrl extends AbstractScraperController {
	private static final String REQUEST_SUCCESS_MESSAGE = "수집 요청이 정상적으로 전달되었습니다.";
	private static final String ALREADY_RUNNING_MESSAGE = "수집이 이미 실행 중입니다. 나중에 다시 시도 하세요";
	private final HolidayScraperService holidayScraperService;

	@Operation(
		summary = "공휴일 수집 요청",
		description = "여러 국가(countries)와 연도(year)를 받아서 공휴일 수집을 시작합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ApiResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@PostMapping("/holiday")
	public ResponseEntity requestCountryScrap(

		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "수집할 국가 목록과 연도를 담은 JSON",
			required = true,
			content = @Content(schema = @Schema(implementation = HolidayScrapRequest.class))
		)
		@RequestBody
		HolidayScrapRequest holidayScrapRequest) {
		List<String> countries = holidayScrapRequest.countries();
		String year = holidayScrapRequest.year();
		if (holidayScraperService.startHolidayScrap(countries, year)) {
			Map<String, Object> response = new HashMap<>();
			response.put("message", REQUEST_SUCCESS_MESSAGE);

			Map<String, String> receiveRequest = new HashMap<>();
			countries.forEach(country -> receiveRequest.put(country, year));
			response.put("receiveRequest", receiveRequest);
			return success(response);
		}
		return success(ALREADY_RUNNING_MESSAGE);
	}
}
