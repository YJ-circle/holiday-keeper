package com.thelightway.planitsquare.task.scraper.common.job;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import com.thelightway.planitsquare.task.scraper.country.batch.CountryJobConfig;
import com.thelightway.planitsquare.task.scraper.holiday.batch.job.HolidayJobConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobListener implements JobExecutionListener {
	private static final AtomicBoolean hasInitialized = new AtomicBoolean(false);
	private static final String INITIALIZED_STATUS_KEY = "initialized";
	private static final String holidayMasterJobName = HolidayJobConfig.HOLIDAY_SCRAP_ALL_COUNTRY_JOB_NAME;
	private static final String holidayRequestJobName = HolidayJobConfig.HOLIDAY_SCRAP_MANUAL_JOB_NAME;
	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;
	private final JobExplorer jobExplorer;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		ExecutionContext ctx = jobExecution.getExecutionContext();
		if (isFirstRun(jobExecution)) {
			log.info(" == 최초 수집 감지 == ");

			log.info(" == 국가 수집 실행 == ");
			JobParameters countryScrapParams = new JobParametersBuilder()
				.addLong("requestTime", System.currentTimeMillis())
				.toJobParameters();
			try {
				Job countryScrapJob = jobRegistry.getJob(CountryJobConfig.COUNTRY_SCRAP_JOB_NAME);
				jobLauncher.run(countryScrapJob, countryScrapParams);
			} catch (JobExecutionException e) {
				//잡 실행 오류
			}

			int currentYear = LocalDate.now().getYear();
			String paramYear = jobExecution.getJobParameters().getString("year");

			for(int i=0; i<6; i++){
				String taskYear = String.valueOf(currentYear - i);
				log.info(" == 기초 값 수집 : " + taskYear + "년");
				if(!taskYear.equals(paramYear)){
					JobParameters holidayScrapParams = new JobParametersBuilder()
						.addString("year", taskYear)
						.addLong("requestTime", System.currentTimeMillis())
						.toJobParameters();
					try {
						Job holidayScrapJob = jobRegistry.getJob(holidayMasterJobName);
						jobLauncher.run(holidayScrapJob, holidayScrapParams);
					} catch (JobExecutionException e) {
						//잡 실행 오류
					}
				}

			}

		}
	}

	private boolean isFirstRun(JobExecution jobExecution) {
		if(!hasInitialized.compareAndSet(false, true)){
			return false;
		}
		int executeCount =
			jobExplorer.getJobInstances(holidayRequestJobName,0,2).size()
			+ jobExplorer.getJobInstances(holidayMasterJobName,0,2).size();

		return executeCount <= 1;
	}
}
