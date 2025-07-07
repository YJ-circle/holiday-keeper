package com.thelightway.planitsquare.task.scraper.country.batch;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.thelightway.planitsquare.task.common.uribuilder.UriBuilderFactory;
import com.thelightway.planitsquare.task.country.dto.Country;
import com.thelightway.planitsquare.task.country.repository.entity.CountryEntity;
import com.thelightway.planitsquare.task.country.repository.entity.CountryJpaRepository;
import com.thelightway.planitsquare.task.country.repository.entity.CountyEntityMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CountryScrapTasklet implements Tasklet {
	private final RestTemplate restTemplate;
	private final URI uri;
	private final CountryJpaRepository countryJpaRepository;

	public CountryScrapTasklet(
		RestTemplate restTemplate,
		@Value("${api.holiday.host}") String host,
		@Value("${api.holiday.path.country}") String path,
		CountryJpaRepository countryJpaRepository) {
		this.restTemplate = restTemplate;
		this.uri = UriBuilderFactory.getBuilder(host).build(path);
		this.countryJpaRepository = countryJpaRepository;
	}

	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.info("==== 국가 정보 수집 시작 ====");
		Country[] countriesArray = restTemplate.getForObject(uri, Country[].class);
		//우선 수집 먼저 작업
		if(countriesArray == null || countriesArray.length == 0) {
			//TODO: API 응답 값 결과가 없을 때 예외처리
			return RepeatStatus.FINISHED;
		}
		saveCountry(countriesArray);
		//TODO: 삭제된 국가는 소프트 삭제 처리
		log.info("==== 국가 정보 수집 완료 ====");
		return RepeatStatus.FINISHED;
	}

	private void saveCountry(Country[] countriesArray) {
		List<CountryEntity> countries = Arrays.stream(countriesArray)
										.map(CountyEntityMapper::toEntity).toList();
		try {
			countryJpaRepository.saveAll(countries);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	private List<Country> deleteDuplicate() {
		return null;
	}
}
