package com.example.hello.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Provider;

public class JdbiModule extends AbstractModule implements ServiceGuiceSupport {

     static private class DatabaseProvider implements Provider<Jdbi> {
         final private Jdbi jdbi;

        public DatabaseProvider() {
            jdbi = Jdbi.create("jdbc:h2:~/test");

            jdbi.useHandle(h -> {
                h.execute("create table contacts (name varchar(100))");
            });
        }

        @Override
        public Jdbi get() {
            return jdbi;
        }
    }

    @Override
    protected void configure() {
        bind(Jdbi.class).toProvider(DatabaseProvider.class);
    }
}
