package com.thelightway.planitsquare.task.country.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.country.service.CountryService;

import lombok.RequiredArgsConstructor;

/**
 * 국가 정보 조회 API 컨트롤러
 */
@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {
	private final CountryService countryService;
	private static final String successMessage = "국가 조회 요청 성공";

	@GetMapping
	public ResponseEntity getCountry() {
		return success(successMessage, countryService.getAllCountry());
	}
}
