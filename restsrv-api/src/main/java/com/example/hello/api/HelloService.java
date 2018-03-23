package com.example.hello.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import org.pcollections.PVector;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * The hello service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the HelloService.
 */
public interface HelloService extends Service {
    /**
     * Example: curl http://localhost:9000/api/hello/Alice
     *
     */
    ServiceCall<NotUsed, PVector<GreetingMessage>> hello(String id);

    /**
     * /api/data/v1/indexlevels/${indexKey}/${effectiveFromDate}/${effectiveToDate}?frequency=DAILY&includeChildren=false
     */
    ServiceCall<NotUsed, ParsedUrlParams> parseUrlParams(String indexKey,
                                                String effectiveFromDate,
                                                String effectiveToDate,
                                                Optional<String> frequency,
                                                Optional<Boolean> includeChildren);

    ServiceCall<NotUsed, PVector<GreetingMessage>> authHello(String id);

    @Override
    default Descriptor descriptor() {
        return named("restsrv")
                .withCalls(
                        pathCall("/api/hello/:id", this::hello),
                        pathCall("/api/auth-hello/:id", this::authHello),
                        pathCall("/api/data/v1/indexlevels/:index/:from/:to?frequency&includeChildren",
                                this::parseUrlParams)
                )
                .withAutoAcl(true);
    }

}

