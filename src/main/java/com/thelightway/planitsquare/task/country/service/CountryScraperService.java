package com.thelightway.planitsquare.task.country.service;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryScraperService {
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final JobLauncher jobLauncher;

	public boolean startCountryScrap() {
		if (!isRunning.compareAndSet(false, true)) {
			return false;
		}

		try {
			//jobStart();
			return true;
		} finally {
			isRunning.set(false);
		}
	}
	private void jobStart(Job job){
		//job 시작
	}
}
