package com.thelightway.planitsquare.task.scraper.country.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.scraper.country.service.CountryScraperService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/scraper")
@RequiredArgsConstructor
public class CountryScraperCtrl {
	private final CountryScraperService countryScraperService;
	private static final String REQUEST_SUCCESS_MESSAGE = "국가 수집 요청이 정상적으로 전달되었습니다.";
	private static final String ALREADY_RUNNING_MESSAGE = "국가 수집이 이미 실행 중입니다. 나중에 다시 시도 하세요";

	@GetMapping("/country/start")
	public ResponseEntity requestCountryScrap() {
		if(countryScraperService.startCountryScrap()){
			return success(REQUEST_SUCCESS_MESSAGE);
		}
		return success(ALREADY_RUNNING_MESSAGE);
	}
}
