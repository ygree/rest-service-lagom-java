package com.example.auth;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface UserStorage {

    CompletionStage<Optional<User>> lookupUser(String username, String password);

}
