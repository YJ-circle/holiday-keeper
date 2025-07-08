package com.thelightway.planitsquare.task.scraper.holiday.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.thelightway.planitsquare.task.scraper.holiday.dto.Holiday;
import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;
import com.thelightway.planitsquare.task.holiday.mapper.HolidayEntityMapper;

@Component
public class HolidayScraperItemProcessor implements ItemProcessor<Holiday, HolidayEntity> {

	@Override
	public HolidayEntity process(Holiday item) throws Exception {
		//TODO 중복 체크
		return HolidayEntityMapper.toEntity(item);
	}
}
