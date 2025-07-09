package com.thelightway.planitsquare.task.scraper.holiday.batch.reader;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.thelightway.planitsquare.task.common.uribuilder.UriBuilderFactory;
import com.thelightway.planitsquare.task.scraper.holiday.batch.dto.Holiday;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@StepScope
public class HolidayScraperItemReader implements ItemReader<Holiday> {
	private final RestTemplate restTemplate;
	private final String host;
	private final String basePath;
	private final String year;
	private Iterator<Holiday> iterator;

	public HolidayScraperItemReader(
		@Value("${api.holiday.host}") String host,
		@Value("${api.holiday.path.holidays}") String basePath,
		@Value("#{jobParameters['year']}") String year,
		RestTemplate restTemplate) {
		this.host = host;
		this.basePath = basePath;
		this.year = year;
		this.restTemplate = restTemplate;
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		if (year == null || year.isEmpty()) {
			//TODO: 추후 예외 처리
			throw new RuntimeException("연도는 빈 값일 수 없습니다.");
		}

		List<Holiday> holidays = new ArrayList<>();
		List<String> countryIds = (List<String>)stepExecution.getExecutionContext().get("ids");
		log.info("Thread: " + Thread.currentThread().getName() + " 공휴일 정보 수집 시작");
		log.info("  - " + countryIds + ": " + year);
		for (String countryId : countryIds) {
			URI uri = UriBuilderFactory.builder(host).build(basePath + "/" + year + "/" + countryId);
			Holiday[] holidayArrays = restTemplate.getForObject(uri, Holiday[].class);
			if (holidayArrays == null || holidayArrays.length == 0) {
				// TODO: 예외처리
			}
			holidays.addAll(Arrays.asList(holidayArrays));
		}
		this.iterator = holidays.iterator();
	}

	@Override
	public Holiday read() throws Exception {
		return (iterator != null && iterator.hasNext()) ? iterator.next() : null;
	}
}
