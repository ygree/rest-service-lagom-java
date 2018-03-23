package com.example.hello.impl;

import akka.NotUsed;
import com.example.auth.AuthService;
import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import com.example.hello.api.ParsedUrlParams;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import com.lightbend.lagom.jdbi.JdbiSession;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

    final private JdbiSession jdbiSession;
    final private AuthService authService;

    @Inject
    public HelloServiceImpl(JdbiSession jdbiSession, AuthService authService) {
        this.jdbiSession = jdbiSession;
        this.authService = authService;

        //NOTE: it's not production code. We create this table here only to simplify this example.
        CompletionStage<Integer> createTable = jdbiSession.withHandle(h ->
                h.execute("create table IF NOT EXISTS contacts (name varchar(100))")
        );

        try {
            createTable.toCompletableFuture().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <Request, Response> ServerServiceCall<Request, Response> logged(
            ServerServiceCall<Request, Response> serviceCall) {
        return HeaderServiceCall.compose(requestHeader -> {
            System.out.println("Received " + requestHeader.method() + " " + requestHeader.uri());
            return serviceCall;
        });
    }

    @Override
    public ServiceCall<NotUsed, PVector<GreetingMessage>> authHello(String id) {
        return logged(authService.authenticated(user -> hello(id)::invoke));
    }

    @Override
    public ServiceCall<NotUsed, PVector<GreetingMessage>> hello(String id) {

        return request -> {

            CompletionStage<PVector<GreetingMessage>> result = jdbiSession.withHandle(h -> {

                // insert a new contact
                h.attach(ContactsDao.class).insertContact(id);

                // select list of all contacts
                List<GreetingMessage> list = h
                        .createQuery("select name from contacts")
                        .map((rs, ctx) -> new GreetingMessage(rs.getString("name")))
                        .list();

                // pack all selected contacts into an immutable collection
                return TreePVector.from(list);
            });

            return result;
        };
    }

    @Override
    public ServiceCall<NotUsed, ParsedUrlParams> parseUrlParams(String indexKey,
                                                                LocalDate effectiveFromDate,
                                                                LocalDate effectiveToDate,
                                                                Optional<String> frequency,
                                                                Optional<Boolean> includeChildren) {

        return request -> {

            ParsedUrlParams result = ParsedUrlParams.builder()
                    .indexKey(indexKey)
                    .effectiveFromDate(effectiveFromDate)
                    .effectiveToDate(effectiveToDate)
                    .frequency(frequency
                            .map(String::toUpperCase)
                            .map(ParsedUrlParams.Frequency::valueOf)
                            .orElse(ParsedUrlParams.Frequency.DAILY)
                    )
                    .includeChildren(includeChildren.orElse(false))
                    .build();

            return completedFuture(result);
        };
    }
}
