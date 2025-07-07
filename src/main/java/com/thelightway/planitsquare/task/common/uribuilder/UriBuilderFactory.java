package com.thelightway.planitsquare.task.common.uribuilder;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import com.thelightway.planitsquare.task.common.uribuilder.exception.UriBuilderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UriBuilderFactory {
	private static final Map<String, UriBuilder> uriBuilderMap = new ConcurrentHashMap<>();

	public static UriBuilder builder(String host) {
		return uriBuilderMap.computeIfAbsent(host, UriBuilder::new);
	}

	public static class UriBuilder {
		private final String HOST;

		private UriBuilder(String host) {
			if (host == null || host.isEmpty()) {
				throw new UriBuilderException(HttpStatus.BAD_REQUEST, "HOST 값은 빈 값일 수 없습니다.");
			}
			this.HOST = host;
		}

		public URI build(String path) {
			if (path == null) {
				log.warn("path 값이 없어, 빈 값으로 설정되었습니다.");
				path = "";
			}

			return UriComponentsBuilder.newInstance()
				.scheme("https")
				.host(HOST)
				.port(443)
				.path(path)
				.build(true)
				.toUri();
		}
	}
}
