package com.thelightway.planitsquare.task.scraper.holiday.batch.processor;

import static com.thelightway.planitsquare.task.common.utils.DateUtils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;
import com.thelightway.planitsquare.task.holiday.mapper.HolidayMapper;
import com.thelightway.planitsquare.task.holiday.repository.HolidayJpaRepository;
import com.thelightway.planitsquare.task.scraper.holiday.batch.dto.Holiday;

@Component
@StepScope
public class HolidayScraperItemProcessor implements ItemProcessor<Holiday, HolidayEntity> {
	/**
	 * 공휴일 수집 작업을 하며 Map에서 수집된 공휴일 Entity를 제거합니다.
	 * 최종 수집 완료 후 Map에 남아있는 공휴일 Entity는 제거된 공휴일이므로, 삭제합니다.
	 * 만약 공휴일 날짜가 같고 새로운 이름으로 공휴일이 등록된 경우
	 * 기존 공휴일은 소프트 삭제되고, 새로운 이름의 공휴일이 새로 저장됩니다.
	 */
	private final Map<String, HolidayEntity> oldHolidayEntities = new HashMap<>();
	private final HolidayJpaRepository holidayJpaRepository;
	private final String requestYear;

	public HolidayScraperItemProcessor(
		HolidayJpaRepository holidayJpaRepository,
		@Value("#{jobParameters['year']}") String year) {

		this.holidayJpaRepository = holidayJpaRepository;
		this.requestYear = year;
	}

	/**
	 * 요청 받은 국가 목록을 읽고, 이전에 저장되어있는 공휴일 목록을 oldHolidayEntities에 저장합니다.
	 */
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		List<String> countryCodes = (List<String>)stepExecution.getExecutionContext().get("ids");
		for (String countryCode : countryCodes) {
			loadOldHolidayEntities(countryCode, requestYear);
		}
	}

	/**
	 * 기존에 저장된 공휴일인지 확인하고,
	 * 기존에 저장된 공휴일이라면 oldHolidayEntities에서 해당 공휴일을 제거하고, null값을 리턴합니다.
	 * null 값을 리턴하면, Jpa Writer로 넘기지 않아 따로 수정, 추가 작업이 진행되지 않습니다.
	 *
	 * 즉, 새로운 공휴일만 저장합니다.
	 * 새로운 공휴일이라면, Entity 객체로 변환 되어 Jpa Writer로 넘깁니다.
	 */
	@Override
	public HolidayEntity process(Holiday item) throws Exception {
		String key = item.date() + item.localName();
		HolidayEntity oldEntity = oldHolidayEntities.remove(key);
		if (oldEntity != null) {
			return null;
		}
		return HolidayMapper.toEntity(item);
	}

	/**
	 * 모든 Step이 완료되고 oldHolidayEntities Map에 남아있는 Entity객체는 현재 수집된 공휴일 목록에 없는 값입니다.
	 * 따라서 삭제를 해야하므로, 소프트 삭제를 수행합니다.
	 */
	@AfterStep
	public ExitStatus afterStep() {
		if (!oldHolidayEntities.isEmpty()) {
			oldHolidayEntities.values().forEach(e -> e.changeActiveStatus(false));
			holidayJpaRepository.saveAll(oldHolidayEntities.values());
		}
		return ExitStatus.COMPLETED;
	}

	private void loadOldHolidayEntities(String countryCode, String requestYear) {
		oldHolidayEntities.clear();
		int year = Integer.parseInt(requestYear);
		List<HolidayEntity> all = holidayJpaRepository.findAllByCountryCodeAndDateBetweenAndActive(
			countryCode, firstDayByYear(year), lastDayByYear(year), true
		);

		for (HolidayEntity e : all) {
			String key = e.getDate() + e.getLocalName();
			oldHolidayEntities.put(key, e);
		}
	}
}
