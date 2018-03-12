package com.example.hello.impl;

import akka.NotUsed;
import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

    final private JdbiSession jdbiSession;

    @Inject
    public HelloServiceImpl(JdbiSession jdbiSession) {
        this.jdbiSession = jdbiSession;

        //TODO: remove create table from here!!!
        CompletionStage<Integer> createTable = jdbiSession.withHandle(h ->
                h.execute("create table contacts (name varchar(100))")
        );

        try {

            createTable.toCompletableFuture().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServiceCall<NotUsed, PVector<GreetingMessage>> hello(String id) {

        return request -> {

//            jdbi.useHandle(h -> { h
//                .createUpdate("insert into contacts(name) values(:name)")
//                .bind("name", id)
//                .execute();
//            });

            CompletionStage<Integer> insert = jdbiSession.withHandle(h -> h
                    .createUpdate("insert into contacts(name) values(:name)")
                    .bind("name", id)
                    .execute()
            );

//            List<GreetingMessage> result = jdbi.withHandle(h -> h
//                    .createQuery("select name from contacts")
//                    .map((rs, ctx) -> new GreetingMessage(rs.getString("name")))
//                    .list() //TODO: create or find PCollection Jdbi Collectors
//                    //TODO: even better if there is there a way to Serialize java8 stream or iterator to json?
//            );

            CompletionStage<PVector<GreetingMessage>> result = jdbiSession.withHandle(h -> {
                List<GreetingMessage> list = h
                        .createQuery("select name from contacts")
                        .map((rs, ctx) -> new GreetingMessage(rs.getString("name")))
                        .list();//TODO: create or find PCollection Jdbi Collectors
//TODO: even better if there is there a way to Serialize java8 stream or iterator to json?

                return TreePVector.from(list);

            });


            return insert.thenCombine(result, (a, b) -> b);

//            List<GreetingMessage> result = Collections.emptyList();

//            return CompletableFuture.completedFuture(TreePVector.from(result));
        };
    }

    private GreetingMessage greeting(String name, int number) {
        return new GreetingMessage("Hello, " + name + "! " + number);
    }

}
