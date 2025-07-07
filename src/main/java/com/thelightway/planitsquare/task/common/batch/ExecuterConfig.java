package com.thelightway.planitsquare.task.common.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecuterConfig {
	public static final String NORMAL_EXECUTOR_NAME = "normalExecutor";

	@Bean(NORMAL_EXECUTOR_NAME)
	public TaskExecutor createNormalExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(10);
		executor.setThreadNamePrefix("normalExecutor-");
		executor.initialize();
		return executor;
	}
}
