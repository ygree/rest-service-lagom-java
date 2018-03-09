package com.example.hello.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import lombok.val;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

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
    public ServiceCall<NotUsed, PVector<GreetingMessage>> hello(String id) {
        return request -> {
            PVector<GreetingMessage> result = TreePVector.<GreetingMessage>empty()
                    .plus(greeting(id, 1))
                    .plus(greeting(id, 1));
            return CompletableFuture.completedFuture(result);
        };
    }

    private GreetingMessage greeting(String name, int number) {
        return new GreetingMessage("Hello, " + name + "! " + number);
    }

}
