package com.thelightway.planitsquare.task.country.batch;

import java.net.URI;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CountryScrapTasklet implements Tasklet {
	private final RestTemplate restTemplate;
	private final URI uri;

	public CountryScrapTasklet(
		RestTemplate restTemplate,
		@Value("${api.holiday.host}") String host,
		@Value("${api.holiday.path.country}") String path) {
		this.restTemplate = restTemplate;
		this.uri = UriBuilderFactory.getBuilder(host).build(path);
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Country[] countriesArray = restTemplate.getForObject(uri, Country[].class);
		//TODO: 변경 점검 및 결과 리턴
		/* TODO: 값 수집 시작
		  - 변경 발생 시 이전 값 삭제(softDelete) //deleteDuplicate();
		  - 새로운 값 수집
		  - 트랜잭션으로 묶기
		 */
		return RepeatStatus.FINISHED;
	}

	private List<Country> deleteDuplicate() {
		return null;
	}
}
