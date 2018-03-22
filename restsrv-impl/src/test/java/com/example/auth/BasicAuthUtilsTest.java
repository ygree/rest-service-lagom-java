package com.example.auth;

import org.junit.Test;

import java.util.Optional;

import static com.example.auth.BasicAuthUtils.parseHeader;
import static org.junit.Assert.assertEquals;

public class BasicAuthUtilsTest {

    @Test
    public void parseHeaderTest() {
        String header = "Basic dGVzdDp0ZXN0";

        assertEquals(Optional.of(new BasicAuthUtils.Credentials("test", "test")), parseHeader(header));
    }

}
