package com.thelightway.planitsquare.task.scraper.country.service;

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
import com.thelightway.planitsquare.task.scraper.country.batch.job.CountryJobConfig;

@Service
public class CountryScraperService {
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final JobLauncher jobLauncher;
	private final Job countryScrapJob;
	private final TaskExecutor normalExecutor;

	public CountryScraperService(
		JobLauncher jobLauncher,
		@Qualifier(CountryJobConfig.COUNTRY_SCRAP_JOB_NAME) Job countryScrapJob,
		@Qualifier(TaskExecutorConfig.NORMAL_EXECUTOR_NAME) TaskExecutor normalExecutor) {

		this.jobLauncher = jobLauncher;
		this.countryScrapJob = countryScrapJob;
		this.normalExecutor = normalExecutor;
	}

	public boolean startCountryScrap() {
		if (!isRunning.compareAndSet(false, true)) {
			return false;
		}
		jobStart();
		return true;
	}

	private void jobStart() {
		JobParameters params = new JobParametersBuilder()
			.addLong("requestTime", System.currentTimeMillis())
			.toJobParameters();
		normalExecutor.execute(() -> {
				try {
					jobLauncher.run(countryScrapJob, params);
				} catch (JobExecutionException e) {
					//TODO: 예외처리 추가
				} finally {
					isRunning.set(false);
				}
			}
		);
	}
}
