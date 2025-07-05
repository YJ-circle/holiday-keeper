package com.thelightway.planitsquare.task.context;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/***
 * H2 데이터베이스와 정상적으로 연결되는지 확인하는 테스트
 *
 * @since 2025-07-05
 */
@SpringBootTest
@Tag("Required-DB")
public class DatabaseConnectionTest {
	@Autowired
	private DataSource dataSource;

	@Test
	@DisplayName("H2 데이터베이스 연결 테스트")
	void testH2Connection() {
		assertDoesNotThrow(() -> {
			try (Connection connection = dataSource.getConnection()) {
				assertThat(connection).isNotNull();
				assertThat(connection.isValid(1)).isTrue();
			}
			System.out.println("H2 DB 연결 성공");
		}, "H2 DB 연결 실패");
	}
}
