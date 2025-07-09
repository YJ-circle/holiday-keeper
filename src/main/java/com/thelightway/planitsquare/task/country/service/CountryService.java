package com.thelightway.planitsquare.task.country.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thelightway.planitsquare.task.country.dto.CountryResponse;
import com.thelightway.planitsquare.task.country.repository.entity.CountryEntityMapper;
import com.thelightway.planitsquare.task.country.repository.entity.CountryJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {
	private final CountryJpaRepository countryJpaRepository;

	public List<CountryResponse> getAllCountry() {
		return countryJpaRepository.findAllByActive(true).stream()
			.map(CountryEntityMapper::toResponse)
			.toList();
	}
}
