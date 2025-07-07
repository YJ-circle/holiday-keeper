package com.thelightway.planitsquare.task.scraper.holiday.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thelightway.planitsquare.task.scraper.holiday.batch.step.HolidayScraperStepConfig;

@Configuration
public class HolidayJobConfig {
	public static final String HOLIDAY_SCRAP_JOB_NAME = "holidayScrapJob";

	@Bean(HOLIDAY_SCRAP_JOB_NAME)
	public Job createCountryScrapJob(JobRepository jobRepository,
		@Qualifier(HolidayScraperStepConfig.holidayScraperMasterStepName)
		Step scrapStep) {
		return new JobBuilder(HOLIDAY_SCRAP_JOB_NAME, jobRepository)
			.start(scrapStep)
			.build();
	}
}
