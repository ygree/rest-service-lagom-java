package com.example.hello.impl;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;

import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.PVector;

public class HelloServiceTest {

    private static TestServer server;

    @BeforeClass
    public static void setUp() {
        server = startServer(defaultSetup().withJdbc());
        //use .configureBuilder(b -> b.overrides(bind(GreetingService.class).to(GreetingStub.class))) to override bindings
    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void testHello() throws Exception {

        HelloService service = server.client(HelloService.class);

        PVector<GreetingMessage> result = service.hello("Yury")
                .invoke().toCompletableFuture().get(5, SECONDS);

        assertTrue(result.contains(new GreetingMessage("Yury")));
    }

    @Test
    public void testAuthHelloSuccess() throws Exception {

        HelloService service = server.client(HelloService.class);

        PVector<GreetingMessage> result = service.authHello("Yury")
                .handleRequestHeader(h -> h.withHeader("Authorization", "Basic dGVzdDp0ZXN0"))
                .invoke()
                .toCompletableFuture().get(5, SECONDS);


        assertTrue(result.contains(new GreetingMessage("Yury")));
    }

    @Test(expected = com.lightbend.lagom.javadsl.api.transport.Forbidden.class) //expected = java.util.concurrent.ExecutionException.class)
    public void testAuthHelloFailure() throws Throwable {

        HelloService service = server.client(HelloService.class);

        Throwable result = service.authHello("Yury")
                .invoke()
                .toCompletableFuture()
                .handle((v, e) -> e)
                .get(5, SECONDS);

        throw result.getCause();
    }

}
