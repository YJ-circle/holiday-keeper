package com.thelightway.planitsquare.task.common;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate rest = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(List.of(
			MediaType.APPLICATION_JSON,
			MediaType.valueOf("text/json"),
			MediaType.valueOf("text/json;charset=utf-8")
		));
		rest.getMessageConverters().addFirst(converter);
		return rest;
	}
}
