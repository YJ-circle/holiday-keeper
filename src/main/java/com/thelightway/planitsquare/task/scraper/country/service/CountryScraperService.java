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

import com.thelightway.planitsquare.task.common.batch.ExecuterConfig;
import com.thelightway.planitsquare.task.scraper.country.batch.CountryJobConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryScraperService {
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final JobLauncher jobLauncher;
	@Qualifier(CountryJobConfig.COUNTRY_SCRAP_JOB_NAME)
	private final Job countryScrapJob;

	@Qualifier(ExecuterConfig.NORMAL_EXECUTOR_NAME)
	private final TaskExecutor normalExecutor;

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
					//잡 실행 오류
				} finally {
					isRunning.set(false);
				}
			}
		);
	}
}
