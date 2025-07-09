package com.thelightway.planitsquare.task.scraper.country.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.thelightway.planitsquare.task.scraper.country.batch.tasklet.CountryScrapTasklet;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CountryStepConfig {
	public static final String COUNTRY_SCRAP_STEP_NAME = "countryScrapStep";
	private final CountryScrapTasklet countryScrapTasklet;

	@Bean(COUNTRY_SCRAP_STEP_NAME)
	public Step createCountryScrapStep(JobRepository jobRepository,
		PlatformTransactionManager tx) {
		return new StepBuilder(COUNTRY_SCRAP_STEP_NAME, jobRepository)
			.tasklet(countryScrapTasklet, tx)
			.build();
	}
}
