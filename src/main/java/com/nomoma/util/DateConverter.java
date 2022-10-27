package com.nomoma.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateConverter {

    public String currentQuarter() {
        int year = LocalDate.now().getYear();
        Month month = LocalDate.now().getMonth();
        int quarter = month.equals(Month.JANUARY) || month.equals(Month.FEBRUARY) || month.equals(Month.MARCH) ? 1 :
                      month.equals(Month.APRIL) || month.equals(Month.MAY) || month.equals(Month.JUNE) ? 2 :
                      month.equals(Month.JULY) || month.equals(Month.APRIL) || month.equals(Month.SEPTEMBER) ? 3 : 4;

        return year + "-" + quarter + "Q";
    }

    public String currentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        return format.format(new Date(System.currentTimeMillis()));
    }
}
