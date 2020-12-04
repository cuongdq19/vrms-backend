package org.lordrose.vrms.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class DateTimeUtils {

    public static LocalDateTime toLocalDateTime(Long seconds) {
        Instant instant = Instant.ofEpochSecond(seconds);
        ZonedDateTime zoned = instant.atZone(ZoneId.systemDefault());
        return zoned.toLocalDateTime();
    }

    public static LocalTime toLocalTime(Long seconds) {
        return toLocalDateTime(seconds).toLocalTime();
    }

    public static Long toSeconds(LocalDateTime dateTime) {
        return dateTime.atZone(TimeZone.getDefault().toZoneId()).toEpochSecond();
    }

    public static Long toSeconds(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        return toSeconds(dateTime);
    }
}
