package com.thelightway.planitsquare.task.scraper.country.batch.tasklet;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.thelightway.planitsquare.task.common.uribuilder.UriBuilderFactory;
import com.thelightway.planitsquare.task.scraper.country.batch.dto.Country;
import com.thelightway.planitsquare.task.country.repository.entity.CountryEntity;
import com.thelightway.planitsquare.task.country.repository.entity.CountryEntityMapper;
import com.thelightway.planitsquare.task.country.repository.entity.CountryJpaRepository;
import com.thelightway.planitsquare.task.scraper.exception.DatabaseWriteException;
import com.thelightway.planitsquare.task.scraper.exception.EmptyApiResponseException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 국가 정보 수집 Tasklet
 *
 * <p>이 Tasklet은 공휴일 API를 호출하여 국가 정보를 조회한 뒤, 다음과 같은 일련의 작업을 수행합니다:
 * <ol>
 *   <li>신규 또는 변경된 국가 정보를 데이터베이스에 저장(upsert)</li>
 *   <li>이전에 비활성화된 국가 레코드를 물리 삭제(purge)</li>
 *   <li>API 응답에 더 이상 포함되지 않은 국가를 소프트 삭제(active=false) 처리</li>
 * </ol>
 */
@Slf4j
@Component
public class CountryScrapTasklet implements Tasklet {
	private final RestTemplate restTemplate;
	private final URI uri;
	private final CountryJpaRepository countryJpaRepository;

	/**
	 * 국가 정보 수집 Tasklet을 실행하기 위한 설정을 생성자를 통해 주입합니다.
	 * @param restTemplate API 요청을 위한 RestTemplate 객체
	 * @param host API 요청을 보낼 host 정보
	 * @param path 국가 정보를 반환해주는 API Path 정보
	 * @param countryJpaRepository 국가 정보를 저장할 JPA 레포지토리 객체
	 */
	public CountryScrapTasklet(
		RestTemplate restTemplate,
		@Value("${api.holiday.host}") String host,
		@Value("${api.holiday.path.country}") String path,
		CountryJpaRepository countryJpaRepository) {

		this.restTemplate = restTemplate;
		this.uri = UriBuilderFactory.builder(host).build(path);
		this.countryJpaRepository = countryJpaRepository;
	}

	/**
	 * 국가 정보 수집 작업을 수행합니다.
	 *
	 * 작업 흐름은 다음과 같습니다:
	 *
	 * 1. requestGetCountry()호출하여 API에서 국가 정보 배열을 조회
	 * 2. 조회된 국가 정보를 DB에 저장/업데이트(upsert)
	 * 3. 이전에 비활성화된 국가 레코드를 물리 삭제(purge)
	 * 4. 현재 API 응답에 없는 국가를 소프트 삭제(active=false) 처리
	 *
	 * @param contribution 현재 Step 의 실행 컨텍스트 정보
	 * @param chunkContext 청크 처리 관련 컨텍스트 정보
	 * @return 모든 처리가 정상 완료되면 {@link RepeatStatus#FINISHED}
	 * @throws EmptyApiResponseException API 응답이 null이거나 빈 배열일 때 발생
	 * @throws DatabaseWriteException    DB 저장·삭제·업데이트 작업 중 오류가 발생할 때 발생
	 */
	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.info("==== 국가 정보 수집 시작 ====");
		Country[] countriesArray = requestGetCountry();

		if (countriesArray == null || countriesArray.length == 0) {
			throw new EmptyApiResponseException("공휴일 API 응답이 null이거나 빈 배열입니다. URI=" + uri);
		}

		saveCountry(countriesArray);
		purgeInactiveCountries();
		softDeleteInactiveCountries(countriesArray);

		log.info("==== 국가 정보 수집 완료 ====");
		return RepeatStatus.FINISHED;
	}

	/**
	 * 국가 정보 API로 요청을 보내, 국가 정보들을 배열로 받습니다.
	 * @return API 응답 결과 (국가 정보 배열)
	 */
	private Country[] requestGetCountry() {
		return restTemplate.getForObject(uri, Country[].class);
	}

	/**
	 * 수집된 국가 정보를 데이터 베이스에 저장 합니다.
	 * @param countriesArray API 응답으로 받은 국가 정보 배열
	 */
	private void saveCountry(Country[] countriesArray) {
		log.info(" - 국가 정보 수집 결과 저장");
		List<CountryEntity> countries = toEntityListFromCountriesArrays(countriesArray);
		try {
			countryJpaRepository.saveAll(countries);
		} catch (Exception e) {
			throw new DatabaseWriteException("국가 정보 저장 실패", e);
		}
	}

	/**
	 * 이전에 이미 삭제됐던 국가를 데이터베이스에서 완전 삭제합니다.
	 */
	private void purgeInactiveCountries() {
		try {
			countryJpaRepository.deleteAll(countryJpaRepository.findAllByActive(false));
		} catch (Exception e) {
			throw new DatabaseWriteException("이전에 삭제된 국가 DB 삭제 실패", e);
		}
	}

	/**
	 * 활성 상태인 국가 중 현재 수집 작업에 포함되지 않은 국가를 소프트 삭제 합니다.
	 * @param updateCountryArr API 응답으로 수집된 국가 코드 배열
	 */
	private void softDeleteInactiveCountries(Country[] updateCountryArr) {
		Set<String> updateCountries = toSetCountryCodeFromCountryArrays(updateCountryArr);
		try {
			List<CountryEntity> toSoftDeleteEntities = findCountriesToSoftDelete(updateCountries);
			countryJpaRepository.saveAll(toSoftDeleteEntities);
		} catch (Exception e) {
			throw new DatabaseWriteException("삭제된 국가 정보 소프트 삭제 실패", e);
		}
	}

	/**
	 * 국가 배열을 Entity List 타입으로 변환합니다.
	 * @param countriesArray API 응답으로 수집된 국가 코드 배열
	 * @return 저장할 국가 Entity 리스트
	 */
	private List<CountryEntity> toEntityListFromCountriesArrays(Country[] countriesArray) {
		return Arrays.stream(countriesArray).map(CountryEntityMapper::toEntity).toList();
	}

	/**
	 * 국가 배열을 국가 코드 Set 타입으로 변환합니다.
	 * @param updateCountryArr API 응답으로 수집된 국가 코드 배열
	 * @return 국가 코드 Set
	 */
	private Set<String> toSetCountryCodeFromCountryArrays(Country[] updateCountryArr) {
		return Arrays.stream(updateCountryArr).map(Country::code).collect(Collectors.toSet());
	}

	/**
	 * updateCountries에 포함되지 않은(active=true) 국가 엔티티를 찾아 <br/>
	 * changeDeleteStatus(false)를 통해 소프트 삭제(비활성화) 처리하고, <br/>
	 * 처리된 엔티티 목록을 반환합니다.
	 *
	 * @param updateCountries API 응답으로 수집된 국가 코드 배열
	 * @return 소프트 삭제 처리된 CountryEntity 리스트
	 */
	private List<CountryEntity> findCountriesToSoftDelete(Set<String> updateCountries) {
		try {
			return countryJpaRepository.findAllByActive(true).stream()
				.filter(c -> !updateCountries.contains(c.getCode()))
				.peek(c -> c.changeActiveStatus(false))
				.toList();
		} catch (Exception e) {
			throw new DatabaseWriteException("삭제된 국가 정보 소프트 삭제 실패", e);
		}
	}
}
