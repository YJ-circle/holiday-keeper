package com.thelightway.planitsquare.task.country.repository.entity;

import com.thelightway.planitsquare.task.common.entity.SoftDeletedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CountryEntity extends SoftDeletedEntity {

	@Id
	@Column(name = "country_code", length = 2)
	private String code;

	@Column(name = "country_name", length = 100)
	private String name;

	@Builder
	public CountryEntity(String code, String name) {
		this.code = code;
		this.name = name;
	}

}
