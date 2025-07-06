package com.thelightway.planitsquare.task.context.common;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

import com.thelightway.planitsquare.task.common.uribuilder.UriBuilderFactory;
import com.thelightway.planitsquare.task.common.uribuilder.exception.UriBuilderException;

public class UriBuilderTest {
	@Test
	public void URI_객체_생성_성공_테스트() {
		System.out.println("URI 객체 생성 테스트(성공 상황) 시작");
		String host = "date.nager.at";
		String path = "/api/v3/AvailableCountries";

		UriBuilderFactory.UriBuilder hoildayUriBuilder = UriBuilderFactory.getBuilder(host);
		URI countryUri = hoildayUriBuilder.build(path);
		assertThat(countryUri.getHost()).isEqualTo(host);
		assertThat(countryUri.getPath()).isEqualTo(path);
		System.out.println("URI 객체 생성 테스트(성공 상황) 종료");
	}

	@Test
	public void URI_객체_생성_빈값_테스트() {
		System.out.println("URI 객체 생성 테스트(실패 상황) 시작");
		String host = "";
		assertThatThrownBy(() -> {
			UriBuilderFactory.getBuilder(host);
		}).isInstanceOf(UriBuilderException.class);
		System.out.println("URI 객체 생성 테스트(실패 상황) 종료");
	}
}
