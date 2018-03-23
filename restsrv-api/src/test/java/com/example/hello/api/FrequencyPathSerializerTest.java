package com.example.hello.api;

import org.junit.Assert;
import org.junit.Test;
import org.pcollections.PSequence;


public class FrequencyPathSerializerTest {

    final private FrequencyPathParamSerializer serializer = new FrequencyPathParamSerializer();

    @Test
    public void test() {
        ParsedUrlParams.Frequency origFrequency = ParsedUrlParams.Frequency.MONTHLY;

        PSequence<String> serialized = serializer.serialize(origFrequency);

        ParsedUrlParams.Frequency result = serializer.deserialize(serialized);

        Assert.assertEquals(origFrequency, result);
    }
}
