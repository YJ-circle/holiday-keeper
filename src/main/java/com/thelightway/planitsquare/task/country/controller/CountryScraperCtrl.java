package com.thelightway.planitsquare.task.country.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.country.service.CountryScraperService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CountryScraperCtrl {
	private final CountryScraperService countryScraperService;

	public ResponseEntity requestCountryScrap() {
		if(!countryScraperService.startCountryScrap()){
			return success("국가 수집이 이미 실행 중입니다. 나중에 다시 시도 하세요");
		}
		return success("국가 수집을 시작합니다.");
	}
}
