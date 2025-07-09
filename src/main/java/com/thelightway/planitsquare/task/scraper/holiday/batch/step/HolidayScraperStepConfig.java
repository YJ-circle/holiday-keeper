package com.thelightway.planitsquare.task.scraper.holiday.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.thelightway.planitsquare.task.common.batch.TaskExecutorConfig;
import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;
import com.thelightway.planitsquare.task.scraper.exception.RetryableException;
import com.thelightway.planitsquare.task.scraper.holiday.batch.dto.Holiday;
import com.thelightway.planitsquare.task.scraper.holiday.batch.partitioner.HolidayScraperPartitionerByRequest;
import com.thelightway.planitsquare.task.scraper.holiday.batch.partitioner.HolidayScraperPartitionerWithAllCountry;
import com.thelightway.planitsquare.task.scraper.holiday.batch.processor.HolidayScraperItemProcessor;
import com.thelightway.planitsquare.task.scraper.holiday.batch.reader.HolidayScraperItemReader;
import com.thelightway.planitsquare.task.scraper.holiday.batch.writer.HolidayItemWriter;

@Configuration
public class HolidayScraperStepConfig {
	public static final String HOLIDAY_SCRAPER_ALL_COUNTRY_STEP = "holidayScraperMasterStep";
	public static final String HOLIDAY_SCRAP_MANUAL_STEP = "holidayScraperManualStep";
	public static final String holidayScraperStepName = "holidayScraperSlaveStep";
	private final JobRepository jobRepository;
	private final TaskExecutor taskExecutor;

	public HolidayScraperStepConfig(
		JobRepository jobRepository,
		@Qualifier(TaskExecutorConfig.NORMAL_EXECUTOR_NAME) TaskExecutor taskExecutor
	) {
		this.jobRepository = jobRepository;
		this.taskExecutor = taskExecutor;
	}

	@Bean(HOLIDAY_SCRAPER_ALL_COUNTRY_STEP)
	public Step createMasterStep(@Qualifier(holidayScraperStepName) Step scraperStep,
		HolidayScraperPartitionerWithAllCountry partitioner) {
		return new StepBuilder(HOLIDAY_SCRAPER_ALL_COUNTRY_STEP, jobRepository)
			.partitioner(holidayScraperStepName, partitioner)
			.step(scraperStep)
			.taskExecutor(taskExecutor)
			.gridSize(5)
			.build();
	}

	@Bean(HOLIDAY_SCRAP_MANUAL_STEP)
	public Step createHolidayStepByManual(@Qualifier(holidayScraperStepName) Step scraperStep,
		HolidayScraperPartitionerByRequest partitioner) {
		return new StepBuilder(HOLIDAY_SCRAP_MANUAL_STEP, jobRepository)
			.partitioner(holidayScraperStepName, partitioner)
			.step(scraperStep)
			.taskExecutor(taskExecutor)
			.gridSize(5)
			.build();
	}

	@Bean(holidayScraperStepName)
	public Step createScrapStep(JobRepository jobRepository,
		PlatformTransactionManager txManager,
		HolidayScraperItemReader reader,
		HolidayScraperItemProcessor processor,
		HolidayItemWriter writer) {
		return new StepBuilder(holidayScraperStepName, jobRepository)
			.<Holiday, HolidayEntity>chunk(100, txManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.faultTolerant()
			.retry(RetryableException.class)
			.retryLimit(3)
			.skip(Exception.class)
			.skipLimit(10)
			.build();
	}

}
