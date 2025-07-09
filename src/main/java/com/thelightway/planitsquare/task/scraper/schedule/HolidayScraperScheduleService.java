package com.thelightway.planitsquare.task.scraper.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thelightway.planitsquare.task.common.utils.DateUtils;
import com.thelightway.planitsquare.task.country.dto.CountryResponse;
import com.thelightway.planitsquare.task.country.service.CountryService;
import com.thelightway.planitsquare.task.scraper.country.service.CountryScraperService;
import com.thelightway.planitsquare.task.scraper.holiday.service.HolidayScraperService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class HolidayScraperScheduleService {
	private final HolidayScraperService holidayScraperService;
	private final CountryScraperService countryScraperService;
	private final CountryService countryService;

	/**
	 * 매년 1월 2일 01:00 KST 에 실행됩니다.
	 * 1) 국가 데이터를 먼저 스크래핑하고,
	 * 2) Thread.sleep 으로 잠시 대기하여 DB 반영을 보장한 뒤,
	 * 3) 조회된 국가 코드를 기준으로 금년도·전년도 공휴일을 순차적으로 스크래핑합니다.
	 */
	@SneakyThrows
	@Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
	public void startScheduleJob() {
		countryScraperService.startCountryScrap();
		Thread.sleep(10_000);
		List<String> countries = countryService.getAllCountry()
												.stream()
												.map(CountryResponse::code)
												.toList();
		holidayScraperService.startHolidayScrap(countries, DateUtils.getCurrentYear());
		holidayScraperService.startHolidayScrap(countries, DateUtils.getPreviousYear());
	}
}
