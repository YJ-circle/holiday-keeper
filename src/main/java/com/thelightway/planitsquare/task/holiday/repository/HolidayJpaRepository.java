package com.thelightway.planitsquare.task.holiday.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {
	Page<HolidayEntity> findAllByActive(boolean active, Pageable pageable);

	Page<HolidayEntity> findByCountryCodeAndActive(String countryCode, boolean active, Pageable pageable);

	Page<HolidayEntity> findByCountryCodeAndActiveAndDateBetween(String countryCode, boolean active,
		LocalDate startDate, LocalDate endDate, Pageable pageable);
}
