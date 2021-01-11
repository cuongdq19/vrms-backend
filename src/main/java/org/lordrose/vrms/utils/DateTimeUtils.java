package org.lordrose.vrms.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class DateTimeUtils {

    public static LocalDateTime toLocalDateTime(Long seconds) {
        if (seconds == null)
            return null;
        Instant instant = Instant.ofEpochSecond(seconds);
        ZonedDateTime zoned = instant.atZone(ZoneId.systemDefault());
        return zoned.toLocalDateTime();
    }

    public static LocalTime toLocalTime(Long seconds) {
        if (seconds == null)
            return null;
        return toLocalDateTime(seconds).toLocalTime();
    }

    public static Long toSeconds(LocalDateTime dateTime) {
        if (dateTime == null)
            return null;
        return dateTime.atZone(TimeZone.getDefault().toZoneId()).toEpochSecond();
    }

    public static Long toSeconds(LocalDate date, LocalTime time) {
        if (date == null || time == null)
            return null;
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        return toSeconds(dateTime);
    }

    public static List<DateTimeTuple> getDateTimeTuples(int yearValue) {
        Year year = returnYearIfValid(yearValue);
        List<YearMonth> yearMonths = new ArrayList<>();

        for (int i = 1; i <= 12; i++)
            yearMonths.add(year.atMonth(i));

        return yearMonths.stream()
                .map(DateTimeTuple::from)
                .collect(Collectors.toList());
    }

    private static Year returnYearIfValid(int yearValue) {
        Year currentYear = Year.now();
        if (currentYear.getValue() - 1 <= yearValue && yearValue <= currentYear.getValue()) {
            return Year.of(yearValue);
        }
        throw new RuntimeException("Invalid year value");
    }
}
