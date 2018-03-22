package com.example.auth;

import com.google.common.collect.Streams;
import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class AuthServiceImpl implements AuthService {

    final private UserStorage userStorage;

    @Inject
    public AuthServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public <Request, Response> ServerServiceCall<Request, Response> authenticated(
            Function<User, ServerServiceCall<Request, Response>> serviceCall) {

        return HeaderServiceCall.composeAsync(requestHeader -> {


            PSequence<String> authorization = requestHeader.headers().getOrDefault("Authorization", TreePVector.empty());

            Optional<BasicAuthUtils.Credentials> credentials = authorization.stream()
                    .flatMap(header -> Streams.stream(BasicAuthUtils.parseHeader(header))).findFirst();

            // First lookup user
            CompletionStage<Optional<User>> userLookup = credentials
                    .map(creds -> userStorage.lookupUser(creds.getUsername(), creds.getPassword()))
                    .orElse(completedFuture(Optional.empty()));

            // Then, if it exists, apply it to the service call
            return userLookup.thenApply(maybeUser -> {
                if (maybeUser.isPresent()) {
                    return serviceCall.apply(maybeUser.get());
                } else {
                    throw new Forbidden("User must be authenticated to access this service call");
                }
            });
        });
    }
}

