package com.thelightway.planitsquare.task.scraper.holiday.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.thelightway.planitsquare.task.common.batch.ExecuterConfig;
import com.thelightway.planitsquare.task.scraper.holiday.batch.partitioner.HolidayScraperPartitioner;
import com.thelightway.planitsquare.task.scraper.holiday.batch.processor.HolidayScraperItemProcessor;
import com.thelightway.planitsquare.task.scraper.holiday.batch.reader.HolidayScraperItemReader;
import com.thelightway.planitsquare.task.scraper.holiday.batch.writer.HolidayItemWriter;
import com.thelightway.planitsquare.task.scraper.holiday.dto.Holiday;
import com.thelightway.planitsquare.task.scraper.holiday.entity.HolidayEntity;

@Configuration
public class HolidayScraperStepConfig {
	public static final String holidayScraperMasterStepName = "holidayScraperMasterStep";
	public static final String holidayScraperStepName = "holidayScraperSlaveStep";
	private final HolidayScraperPartitioner holidayScraperPartitioner;
	private final JobRepository jobRepository;
	private final TaskExecutor taskExecutor;

	public HolidayScraperStepConfig(
		HolidayScraperPartitioner holidayScraperPartitioner,
		JobRepository jobRepository,
		@Qualifier(ExecuterConfig.NORMAL_EXECUTOR_NAME) TaskExecutor taskExecutor
	) {
		this.holidayScraperPartitioner = holidayScraperPartitioner;
		this.jobRepository = jobRepository;
		this.taskExecutor = taskExecutor;
	}

	@Bean(holidayScraperMasterStepName)
	public Step createMasterStep(@Qualifier(holidayScraperStepName) Step scraperStep) {
		return new StepBuilder(holidayScraperMasterStepName, jobRepository)
			.partitioner(holidayScraperStepName, holidayScraperPartitioner)
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
			.retry(Exception.class)
			.retryLimit(3)
			.build();
	}

}
