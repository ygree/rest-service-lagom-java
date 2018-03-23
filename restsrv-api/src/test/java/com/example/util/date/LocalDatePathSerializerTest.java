package com.example.util.date;

import org.junit.Assert;
import org.junit.Test;
import org.pcollections.PSequence;

import java.time.LocalDate;

public class LocalDatePathSerializerTest {

    final private LocalDatePathSerializer serializer = new LocalDatePathSerializer();

    @Test
    public void test() {
        LocalDate origDate = LocalDate.now();
        PSequence<String> serialized = serializer.serialize(origDate);

        LocalDate result = serializer.deserialize(serialized);

        Assert.assertEquals(origDate, result);
    }
}
