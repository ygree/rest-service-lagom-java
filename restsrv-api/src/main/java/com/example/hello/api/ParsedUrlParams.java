package com.example.hello.api;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class ParsedUrlParams {

    public enum Frequency {
        // do not rename, parser uses this names as is
        DAILY, MONTHLY, QUARTERLY
    }

    String indexKey;
    LocalDate effectiveFromDate;
    LocalDate effectiveToDate;
    Frequency frequency;
    boolean includeChildren;
}
