package com.example.auth;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class UserStorageConst implements UserStorage {

    @Override
    public CompletionStage<Optional<User>> lookupUser(String username, String password) {
        if ("test".equals(username) && "test".equals(password)) {
            return completedFuture(Optional.of(new User(username)));
        }
        return completedFuture(Optional.empty());
    }
}
