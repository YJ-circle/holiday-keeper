package com.thelightway.planitsquare.task.scraper.common.job;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import com.thelightway.planitsquare.task.common.utils.DateUtils;
import com.thelightway.planitsquare.task.scraper.country.batch.job.CountryJobConfig;
import com.thelightway.planitsquare.task.scraper.holiday.batch.job.HolidayJobConfig;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 공휴일 수집이 처음인지 체크하고,
 * 처음이라면 5개년치 모든 국가 공휴일 정보를 수집합니다.
 *
 * 요구사항은 5개년으로 되어있으나,
 * 실제 예시는 2020년 ~ 2025년으로 작성되어 있어
 * 구현은 6개년으로 되어있습니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JobListener implements JobExecutionListener {
	private static final AtomicBoolean hasInitialized = new AtomicBoolean(false);
	private static final String holidayMasterJobName = HolidayJobConfig.HOLIDAY_SCRAP_ALL_COUNTRY_JOB_NAME;
	private static final String holidayRequestJobName = HolidayJobConfig.HOLIDAY_SCRAP_MANUAL_JOB_NAME;
	private final JobLauncher jobLauncher;
	private final JobRegistry jobRegistry;
	private final JobExplorer jobExplorer;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (isFirstRun()) {
			log.info(" == 최초 수집 감지 == ");
			log.info(" == 국가 수집 실행 == ");
			startScrapCountries();

			int currentYear = DateUtils.getCurrentYear();
			String paramYear = jobExecution.getJobParameters().getString("year");

			for (int i = 0; i < 6; i++) {
				String taskYear = String.valueOf(currentYear - i);
				if (!taskYear.equals(paramYear)) { //수집 요청한 연도와 같은 연도는 수집하지 않습니다.
					log.info(" == 기초 값 수집 : " + taskYear + "년");
					runScrapHolidayByYear(taskYear);
				}

			}

		}
	}

	@SneakyThrows
	private void runScrapHolidayByYear(String taskYear) {
		//TODO: 예외 처리 추가
		JobParameters holidayScrapParams = new JobParametersBuilder()
			.addString("year", taskYear)
			.addLong("requestTime", System.currentTimeMillis())
			.toJobParameters();
		Job holidayScrapJob = jobRegistry.getJob(holidayMasterJobName);
		jobLauncher.run(holidayScrapJob, holidayScrapParams);
	}

	@SneakyThrows
	private void startScrapCountries() {
		//TODO: 예외 처리 추가
		JobParameters countryScrapParams = new JobParametersBuilder()
			.addLong("requestTime", System.currentTimeMillis())
			.toJobParameters();
		Job countryScrapJob = jobRegistry.getJob(CountryJobConfig.COUNTRY_SCRAP_JOB_NAME);
		jobLauncher.run(countryScrapJob, countryScrapParams);
	}

	/**
	 * 공휴일 배치 Job이 최초 실행인지 판별합니다.
	 *
	 * 1) hasInitialized 플래그에 대해 원자적 CAS 연산을 수행하여
	 *    여러 스레드가 동시에 최초 실행 로직을 수행하지 않도록 방어합니다.
	 *
	 * 2) JobExplorer를 이용해 과거 생성된 잡 인스턴스 수를 조회하여,
	 *    인스턴스가 1개 이하일 경우에만 최초 실행으로 간주합니다.
	 *    (과거 잡 인스턴스에 대한 정보는 DB에 기록 되어
	 *      서버 재실행시에도 최초 실행이 아님을 확인할 수 있습니다.)
	 */
	private boolean isFirstRun() {
		if (!hasInitialized.compareAndSet(false, true)) {
			return false;
		}
		int executeCount =
			jobExplorer.getJobInstances(holidayRequestJobName, 0, 2).size()
				+ jobExplorer.getJobInstances(holidayMasterJobName, 0, 2).size();

		return executeCount <= 1;
	}
}
