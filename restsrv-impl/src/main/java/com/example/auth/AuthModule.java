package com.example.auth;

import com.google.inject.AbstractModule;

public class AuthModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserStorage.class).to(UserStorageConst.class);
        bind(AuthService.class).to(AuthServiceImpl.class);
    }
}

