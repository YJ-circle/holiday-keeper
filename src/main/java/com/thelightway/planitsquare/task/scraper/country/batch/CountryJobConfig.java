package com.thelightway.planitsquare.task.scraper.country.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountryJobConfig {
	public static final String COUNTRY_SCRAP_JOB_NAME = "countryScrapJob";

	@Bean(COUNTRY_SCRAP_JOB_NAME)
	public Job createCountryScrapJob(JobRepository jobRepository,
									@Qualifier(CountryStepConfig.COUNTRY_SCRAP_STEP_NAME)
									Step scrapStep) {
		return new JobBuilder(COUNTRY_SCRAP_JOB_NAME, jobRepository)
				.start(scrapStep)
				.build();
	}
}
