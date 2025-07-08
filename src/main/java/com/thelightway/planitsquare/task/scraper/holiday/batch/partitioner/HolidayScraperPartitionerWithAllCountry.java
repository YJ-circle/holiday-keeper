package com.thelightway.planitsquare.task.scraper.holiday.batch.partitioner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import com.thelightway.planitsquare.task.country.repository.entity.CountryEntity;
import com.thelightway.planitsquare.task.country.repository.entity.CountryJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HolidayScraperPartitionerWithAllCountry implements Partitioner {
	private final CountryJpaRepository countryJpaRepository;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		List<String> allIds = getCountryIds();
		int total = allIds.size();
		int chunkSize = (int)Math.ceil((double)total / gridSize);

		Map<String, ExecutionContext> result = new HashMap<>(gridSize);

		for (int i = 0; i < gridSize; i++) {
			int start = i * chunkSize;
			int end = Math.min(start + chunkSize, total);
			if (start >= end) {
				break;
			}
			List<String> subList = new ArrayList<>(allIds.subList(start, end));
			ExecutionContext ec = new ExecutionContext();
			ec.put("ids", subList);

			result.put("partition" + i, ec);
		}

		return result;
	}

	private List<String> getCountryIds() {
		return countryJpaRepository.findAllByActive(true)
			.stream().map(CountryEntity::getCode)
			.toList();
	}
}