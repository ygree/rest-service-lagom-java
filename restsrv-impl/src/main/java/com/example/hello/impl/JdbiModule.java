package com.example.hello.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.CompletionStage;

public class JdbiModule extends AbstractModule implements ServiceGuiceSupport {

     static private class DatabaseProvider implements Provider<JdbiSession> {
        final private Provider<JdbcSession> jdbcSession;

        @Inject
        public DatabaseProvider(Provider<JdbcSession> jdbcSession) {
            this.jdbcSession = jdbcSession;
//            this.jdbiSession = new JdbiSession(jdbcSession);

//            CompletionStage<Integer> createTable = jdbcSession.withConnection(c ->
//                    Jdbi.create(() -> c)
//                            .withHandle(h ->
//                                    h.execute("create table contacts (name varchar(100))")
//                            )
//            );
//
//            try {
//
//                createTable.toCompletableFuture().get();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            jdbi = Jdbi.create()
//            jdbi.useHandle(h -> {
//                h.execute("create table contacts (name varchar(100))");
//            });
        }

        @Override
        public JdbiSession get() {
            return new JdbiSession(jdbcSession.get());
        }
    }

    @Override
    protected void configure() {
        bind(JdbiSession.class).toProvider(DatabaseProvider.class);
    }
}
