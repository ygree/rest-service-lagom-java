package com.lightbend.lagom.jdbi;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * This module provides JdbiSession that binds Lagom JdbcSession with Jdbi interface.
 */
public class JdbiModule extends AbstractModule implements ServiceGuiceSupport {

     static private class JdbiSessionProvider implements Provider<JdbiSession> {
        final private Provider<JdbcSession> jdbcSessionProvider;

        @Inject
        public JdbiSessionProvider(Provider<JdbcSession> jdbcSessionProvider) {
            this.jdbcSessionProvider = jdbcSessionProvider;
        }

        @Override
        public JdbiSession get() {
            return new JdbiSession(jdbcSessionProvider.get());
        }
    }

    @Override
    protected void configure() {
        bind(JdbiSession.class).toProvider(JdbiSessionProvider.class);
    }
}
