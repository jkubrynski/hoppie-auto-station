package com.kubrynski.hoppie_autorespond;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

class DateTimeProvider {

    private final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder().appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .toFormatter();

    private LocalTime mockTime;

    DateTimeProvider() {
    }

    String currentTimePlusMinutes(int minutesToAdd) {
        LocalTime now = LocalTime.now();
        if (mockTime != null) {
            now = mockTime;
        }
        return now.plusMinutes(minutesToAdd).format(timeFormatter);
    }

    void setMockTime(LocalTime mockTime) {
        this.mockTime = mockTime;
    }
}
