package com.example.util.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser {
    static private DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    static public LocalDate parse_yyyyMMdd(String str) {
        return LocalDate.parse(str, yyyyMMdd);
    }
}
