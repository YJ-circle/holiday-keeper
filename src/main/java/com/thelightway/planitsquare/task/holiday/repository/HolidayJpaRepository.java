package com.thelightway.planitsquare.task.holiday.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thelightway.planitsquare.task.holiday.entity.HolidayEntity;

public interface HolidayJpaRepository extends JpaRepository<HolidayEntity, Long> {

	List<HolidayEntity> findAllByCountryCodeAndDateBetweenAndActive(
		String countryCode, LocalDate startDate, LocalDate endDate, boolean active);

	Page<HolidayEntity> findAllByActive(boolean active, Pageable pageable);

	Page<HolidayEntity> findByCountryCodeAndActive(String countryCode, boolean active, Pageable pageable);

	Page<HolidayEntity> findByCountryCodeAndDateBetweenAndActive(
		String countryCode, LocalDate startDate, LocalDate endDate, boolean active, Pageable pageable);

	Page<HolidayEntity> findByDateBetweenAndActive(
		LocalDate startDate, LocalDate endDate, boolean active, Pageable pageable);
}
