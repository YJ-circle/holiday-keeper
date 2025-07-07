package com.thelightway.planitsquare.task.country.repository.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryJpaRepository extends JpaRepository<CountryEntity, String> {
	List<CountryEntity> findAllByActive(boolean active);
}
