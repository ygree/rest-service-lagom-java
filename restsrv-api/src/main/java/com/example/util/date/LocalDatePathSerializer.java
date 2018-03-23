package com.example.util.date;

import com.lightbend.lagom.javadsl.api.deser.PathParamSerializer;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDatePathSerializer implements PathParamSerializer<LocalDate> {
    static private DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public PSequence<String> serialize(LocalDate date) {
        return TreePVector.singleton(yyyyMMdd.format(date));
    }

    @Override
    public LocalDate deserialize(PSequence<String> parameters) {
        return yyyyMMdd.parse(parameters.get(0), LocalDate::from);
    }
}
