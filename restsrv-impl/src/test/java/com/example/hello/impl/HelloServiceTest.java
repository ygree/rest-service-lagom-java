package com.example.hello.impl;

import com.example.hello.api.GreetingMessage;
import com.example.hello.api.HelloService;
import com.example.hello.api.ParsedUrlParams;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.PVector;

import java.time.LocalDate;
import java.util.Optional;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void parseUrlParamsTest() throws Exception {
        HelloService service = server.client(HelloService.class);

        ParsedUrlParams parsedUrlParams = service.parseUrlParams(
                "indexKey",
                "20180101",
                "20180228",
                Optional.of("MONTHLY"),
                Optional.of(false))
                .invoke().toCompletableFuture().get(5, SECONDS);

        ParsedUrlParams expected = ParsedUrlParams.builder()
                .indexKey("indexKey")
                .effectiveFromDate(LocalDate.of(2018, 1, 1))
                .effectiveToDate(LocalDate.of(2018, 2, 28))
                .frequency(ParsedUrlParams.Frequency.MONTHLY)
                .includeChildren(false)
                .build();

        assertEquals(expected, parsedUrlParams);
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

