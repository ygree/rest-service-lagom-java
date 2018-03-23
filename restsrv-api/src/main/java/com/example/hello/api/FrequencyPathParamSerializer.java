package com.example.hello.api;

import com.lightbend.lagom.javadsl.api.deser.PathParamSerializer;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

public class FrequencyPathParamSerializer implements PathParamSerializer<ParsedUrlParams.Frequency> {
    @Override
    public PSequence<String> serialize(ParsedUrlParams.Frequency parameter) {
        return TreePVector.singleton(parameter.name());
    }

    @Override
    public ParsedUrlParams.Frequency deserialize(PSequence<String> parameters) {
        return parameters.stream().findFirst().map(ParsedUrlParams.Frequency::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("Expected Frequency param"));
    }
}
