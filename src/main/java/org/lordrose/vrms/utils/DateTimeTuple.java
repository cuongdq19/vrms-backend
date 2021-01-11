package org.lordrose.vrms.utils;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class DateTimeTuple {

    public LocalDateTime from;
    public LocalDateTime to;

    static DateTimeTuple from(YearMonth yearMonth) {
        DateTimeTuple result = new DateTimeTuple();
        result.from = yearMonth.atDay(1).atTime(0,0);
        result.to = yearMonth.atEndOfMonth().atTime(0, 0).plusDays(1);
        return result;
    }
}
