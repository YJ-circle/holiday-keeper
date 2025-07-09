package com.thelightway.planitsquare.task.scraper.holiday.batch.partitioner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Scrap Controller로 요청을 받은 경우 비동기로 국가별 공휴일 정보를 수집하기 위한 파티셔너
 */
@Slf4j
@Component
@StepScope
public class HolidayScraperPartitionerByRequest implements Partitioner {
	private final String requestCountries;

	public HolidayScraperPartitionerByRequest(
		@Value("#{jobParameters['countryCodes']}") String countries
	) {
		this.requestCountries = countries;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		String countriesText = requestCountries;
		List<String> countries = Arrays.asList(countriesText.split(","));
		int total = countries.size();
		int chunkSize = (int)Math.ceil((double)total / gridSize);

		Map<String, ExecutionContext> result = new HashMap<>(gridSize);

		for (int i = 0; i < gridSize; i++) {
			int start = i * chunkSize;
			int end = Math.min(start + chunkSize, total);
			if (start >= end) {
				break;
			}
			List<String> subList = new ArrayList<>(countries.subList(start, end));
			ExecutionContext ec = new ExecutionContext();
			ec.put("ids", subList);

			result.put("partition" + i, ec);
		}

		return result;
	}
}