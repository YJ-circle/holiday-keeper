package com.thelightway.planitsquare.task.country.controller;

import static com.thelightway.planitsquare.task.common.response.ApiResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thelightway.planitsquare.task.country.service.CountryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryController {
	private final CountryService countryService;

	@GetMapping
	public ResponseEntity getCountry() {
		return success(countryService.getAllCountry());
	}
}
