package com.example.auth;

import com.lightbend.lagom.javadsl.server.ServerServiceCall;

import java.util.function.Function;

public interface AuthService {

    <Request, Response> ServerServiceCall<Request, Response> authenticated(
        Function<User, ServerServiceCall<Request, Response>> serviceCall
    );
}
