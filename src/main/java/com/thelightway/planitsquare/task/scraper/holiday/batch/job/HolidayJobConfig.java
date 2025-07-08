package com.thelightway.planitsquare.task.scraper.holiday.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thelightway.planitsquare.task.scraper.common.job.JobListener;
import com.thelightway.planitsquare.task.scraper.holiday.batch.step.HolidayScraperStepConfig;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HolidayJobConfig {
	public static final String HOLIDAY_SCRAP_ALL_COUNTRY_JOB_NAME = "holidayScrapJob";
	public static final String HOLIDAY_SCRAP_MANUAL_JOB_NAME = "holidayScrapManualJob";
	private final JobListener jobListener;

	@Bean(HOLIDAY_SCRAP_ALL_COUNTRY_JOB_NAME)
	public Job createHolidayScrapJobWithAll(JobRepository jobRepository,
		@Qualifier(HolidayScraperStepConfig.HOLIDAY_SCRAPER_ALL_COUNTRY_STEP)
		Step scrapStep) {
		return new JobBuilder(HOLIDAY_SCRAP_ALL_COUNTRY_JOB_NAME, jobRepository)
			.start(scrapStep)
			.listener(jobListener)
			.build();
	}

	@Bean(HOLIDAY_SCRAP_MANUAL_JOB_NAME)
	public Job createHolidayScrapJobByManual(JobRepository jobRepository,
		@Qualifier(HolidayScraperStepConfig.HOLIDAY_SCRAP_MANUAL_STEP)
		Step scrapStep) {
		return new JobBuilder(HOLIDAY_SCRAP_MANUAL_JOB_NAME, jobRepository)
			.start(scrapStep)
			.listener(jobListener)
			.build();
	}
}
