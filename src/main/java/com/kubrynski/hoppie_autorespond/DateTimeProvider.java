package com.kubrynski.hoppie_autorespond;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

class DateTimeProvider {

    private final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder().appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .toFormatter();

    private Clock clock = Clock.systemUTC();

    String currentTimePlusMinutes(int minutesToAdd) {
        return ZonedDateTime.now(clock).plusMinutes(minutesToAdd).format(timeFormatter);
    }

    void setClock(Clock clock) {
        this.clock = clock;
    }
}
