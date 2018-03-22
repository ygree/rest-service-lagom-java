package com.example.auth;

import lombok.Value;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

public class BasicAuthUtils {

    @Value
    static class Credentials {
        String username;
        String password;
    }

    static Optional<Credentials> parseHeader(String header) {
        if (header.startsWith("Basic")) {
            String base64Credentials = header.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            final String[] values = credentials.split(":", 2);
            if (values.length == 2) {
                return Optional.of(new Credentials(values[0], values[1]));
            }
        }
        return Optional.empty();
    }
}
