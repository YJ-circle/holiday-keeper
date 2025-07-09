package com.thelightway.planitsquare.task.scraper.country.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.scraper.AbstractScraperController;
import com.thelightway.planitsquare.task.scraper.country.service.CountryScraperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Scraper", description = "데이터 수집 API")
@RestController
@RequiredArgsConstructor
public class CountryScraperCtrl extends AbstractScraperController {
	private static final String REQUEST_SUCCESS_MESSAGE = "국가 수집 요청이 정상적으로 전달되었습니다.";
	private static final String ALREADY_RUNNING_MESSAGE = "국가 수집이 이미 실행 중입니다. 나중에 다시 시도 하세요";
	private final CountryScraperService countryScraperService;

	@Operation(summary = "국가 정보 수집 요청", description = "외부 API로부터 모든 국가 정보를 수집하여 DB에 저장합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "요청 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@PostMapping("/countries")
	public ResponseEntity requestCountryScrap() {
		if (countryScraperService.startCountryScrap()) {
			return success(REQUEST_SUCCESS_MESSAGE);
		}
		return success(ALREADY_RUNNING_MESSAGE);
	}
}
