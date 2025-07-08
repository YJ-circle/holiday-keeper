package com.thelightway.planitsquare.task.holiday.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.thelightway.planitsquare.task.common.entity.SoftDeletedEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holiday")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HolidayEntity extends SoftDeletedEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "local_name", nullable = false)
	private String localName;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "country_code", length = 2, nullable = false)
	private String countryCode;

	@Column(name = "is_fixed", nullable = false)
	private boolean fixed;

	@Column(name = "is_global", nullable = false)
	private boolean global;

	@Column(name = "launch_year")
	private Integer launchYear;

	@Builder.Default
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "holiday_counties",
		joinColumns = @JoinColumn(name = "holiday_id")
	)
	@Column(name = "county_code", length = 5)
	private Set<String> counties = new HashSet<>();

	@Builder.Default
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "holiday_types",
		joinColumns = @JoinColumn(name = "holiday_id")
	)
	@Column(name = "type", length = 20)
	private Set<String> types = new HashSet<>();

}
