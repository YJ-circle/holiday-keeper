package com.thelightway.planitsquare.task.scraper.holiday.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.thelightway.planitsquare.task.common.batch.TaskExecutorConfig;
import com.thelightway.planitsquare.task.scraper.holiday.batch.job.HolidayJobConfig;

@Service
public class HolidayScraperService {
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final JobLauncher jobLauncher;
	private final Job holidayScrapJob;
	private final TaskExecutor normalExecutor;

	public HolidayScraperService(
		JobLauncher jobLauncher,
		@Qualifier(HolidayJobConfig.HOLIDAY_SCRAP_MANUAL_JOB_NAME) Job holidayScrapJob,
		@Qualifier(TaskExecutorConfig.NORMAL_EXECUTOR_NAME) TaskExecutor normalExecutor
	) {
		this.jobLauncher = jobLauncher;
		this.holidayScrapJob = holidayScrapJob;
		this.normalExecutor = normalExecutor;
	}

	public boolean startHolidayScrap(List<String> countries, int year) {
		if (!isRunning.compareAndSet(false, true)) {
			return false;
		}
		String csv = String.join(",", countries);
		jobStart(csv, year);
		return true;
	}

	private void jobStart(String countries, int year) {

		JobParameters params = new JobParametersBuilder()
			.addString("countryCodes", countries)
			.addString("year", String.valueOf(year))
			.addLong("requestTime", System.currentTimeMillis())
			.toJobParameters();
		normalExecutor.execute(() -> {
				try {
					jobLauncher.run(holidayScrapJob, params);
				} catch (JobExecutionException e) {
					//TODO: 예외처리 추가
				} finally {
					isRunning.set(false);
				}
			}
		);
	}
}
