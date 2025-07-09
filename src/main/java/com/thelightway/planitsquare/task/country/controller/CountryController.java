package com.thelightway.planitsquare.task.country.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.country.service.CountryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 국가 정보 조회 API 컨트롤러
 */
@Tag(name = "Country", description = "국가 정보 API")
@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {
	private final CountryService countryService;
	private static final String successMessage = "국가 조회 요청 성공";

	@Operation(summary = "전체 국가 목록 조회", description = "DB에 저장된 모든 국가 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping
	public ResponseEntity getCountry() {
		return success(successMessage, countryService.getAllCountry());
	}
}
