package com.example.hello.impl;

import akka.NotUsed;
import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

    @Inject
    public HelloServiceImpl() {

    }

    @Override
    public ServiceCall<NotUsed, GreetingMessage> hello(String id) {
        return request -> {
            return CompletableFuture.completedFuture(new GreetingMessage("Hello, " + id + "!"));
        };
    }

}
