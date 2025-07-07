package com.thelightway.planitsquare.task.scraper.holiday.batch.writer;

import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import com.thelightway.planitsquare.task.scraper.holiday.entity.HolidayEntity;

import jakarta.persistence.EntityManagerFactory;

@Component
public class HolidayItemWriter extends JpaItemWriter<HolidayEntity> {
	public HolidayItemWriter(EntityManagerFactory emf) {
		this.setEntityManagerFactory(emf);
	}
}
