package com.example.hello.impl;

import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.concurrent.CompletionStage;

public class JdbiSession {
    final private JdbcSession jdbcSession;

    public JdbiSession(JdbcSession jdbcSession) {
        this.jdbcSession = jdbcSession;
    }

    public <R> CompletionStage<R> withHandle(HandleCallback<R, SQLException> callback) {
        return jdbcSession.withConnection(c -> {
            try {
                return Jdbi.create(() -> c)
                        .withHandle(h ->
                                callback.withHandle(h)
                        );
            } catch (Throwable ex) {
                throw new SQLException(ex);
            }
        });
    }

}
