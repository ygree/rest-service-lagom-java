package com.example.hello.impl;

import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.example.auth.AuthService;
import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import com.lightbend.lagom.jdbi.JdbiSession;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

    final private JdbiSession jdbiSession;
    final private AuthService authService;

    final private ActorRef foo;

    @Inject
    public HelloServiceImpl(JdbiSession jdbiSession, AuthService authService, ActorSystem system) {
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

        this.foo = system.actorOf(Props.create(Foo.class, Foo::new), "foo");
    }

    static class Foo extends AbstractActor {
        @Override
        public Receive createReceive() {
            return ReceiveBuilder.create()
                    .match(String.class, m -> System.out.print("."))
                    .build();
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
            foo.tell(id, ActorRef.noSender());

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


}
