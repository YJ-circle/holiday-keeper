package com.thelightway.planitsquare.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@OpenAPIDefinition(
	info = @Info(
		title       = "Holiday Keeper API",
		version     = "1.0.0"
	)
)
public class HolidayKeeperApp {

	public static void main(String[] args) {
		SpringApplication.run(HolidayKeeperApp.class, args);
	}

}
