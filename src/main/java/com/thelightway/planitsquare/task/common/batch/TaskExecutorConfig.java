package com.thelightway.planitsquare.task.common.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * TaskExecutor 구성 클래스입니다.
 *
 * 병렬 작업 실행 시 사용 시 ThreadPoolTaskExecutor 빈을 정의하고,
 * normalExecutor 라는 이름으로 스프링 컨텍스트에 등록합니다.
 */
@Configuration
public class TaskExecutorConfig {
	public static final String NORMAL_EXECUTOR_NAME = "normalExecutor";

	/**
	 * normalExecutor 라는 이름으로 ThreadPoolTaskExecutor 빈을 생성합니다.
	 *
	 * 설정 값:
	 * corePoolSize: 5 (초기 스레드 수)
	 * maxPoolSize: 10 (최대 스레드 수)
	 * queueCapacity: 10 (대기 큐 크기)
	 * threadNamePrefix: "normalExecutor-" (스레드 이름 접두사)
	 *
	 * @return 구성된 ThreadPoolTaskExecutor 인스턴스
	 */
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
