package com.thelightway.planitsquare.task.common.utils;

import java.time.LocalDate;

public class DateUtils {
	public static LocalDate firstDayByYear(int year) {
		return LocalDate.of(year, 1, 1);
	}

	public static LocalDate lastDayByYear(int year) {
		return LocalDate.of(year, 12, 31);
	}

	public static int getCurrentYear() {
		return LocalDate.now().getYear();
	}

	public static int getPreviousYear() {
		return LocalDate.now().getYear() - 1;
	}
}
