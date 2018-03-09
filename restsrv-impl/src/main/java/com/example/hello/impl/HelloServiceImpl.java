package com.example.hello.impl;

import akka.NotUsed;
import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import org.jdbi.v3.core.Jdbi;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

    final private Jdbi jdbi;

    @Inject
    public HelloServiceImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public ServiceCall<NotUsed, PVector<GreetingMessage>> hello(String id) {

        return request -> {

            jdbi.useHandle(h -> { h
                .createUpdate("insert into contacts(name) values(:name)")
                .bind("name", id)
                .execute();
            });

            List<GreetingMessage> result = jdbi.withHandle(h -> h
                    .createQuery("select name from contacts")
                    .map((rs, ctx) -> new GreetingMessage(rs.getString("name")))
                    .list() //TODO: create or find PCollection Jdbi Collectors
                    //TODO: even better if there is there a way to Serialize java8 stream or iterator to json?
            );

            return CompletableFuture.completedFuture(TreePVector.from(result));
        };
    }

    private GreetingMessage greeting(String name, int number) {
        return new GreetingMessage("Hello, " + name + "! " + number);
    }

}
