package com.example.util.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class DateParserTest {

    @Test
    public void successTest() {
        LocalDate result = DateParser.parse_yyyyMMdd("20181231");

        Assert.assertEquals(LocalDate.of(2018,12,31), result);
    }

    @Test(expected = java.time.DateTimeException.class)
    public void failureTest() {
        LocalDate result = DateParser.parse_yyyyMMdd("20181232");

        Assert.assertEquals(LocalDate.of(2018,12,31), result);
    }
}
