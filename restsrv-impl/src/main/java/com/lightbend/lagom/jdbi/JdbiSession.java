package com.lightbend.lagom.jdbi;

import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.extension.ExtensionCallback;
import org.jdbi.v3.core.extension.ExtensionConsumer;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletionStage;

/**
 * This class provides interface to Jdbi methods via Lagom JdbcSession.
 */
public class JdbiSession {
    final private JdbcSession jdbcSession;

    public JdbiSession(JdbcSession jdbcSession) {
        this.jdbcSession = jdbcSession;
    }

    public <R> CompletionStage<R> withHandle(HandleCallback<R, Exception> callback) {
        return jdbcSession.withConnection(c -> {
            try {
                return createJdbi(c).withHandle(callback);
            } catch (SQLException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        });
    }

    private Jdbi createJdbi(Connection c) {
        return Jdbi
                .create(() -> c)
                // enable SqlObject plugin
                .installPlugin(new SqlObjectPlugin());
    }

    public CompletionStage<Void> useHandle(HandleConsumer<Exception> callback) {
        return withHandle(handle -> {
           callback.useHandle(handle);
           return null;
        });
    }

    public <R, E> CompletionStage<R> withExtension(Class<E> extensionType, ExtensionCallback<R, E, SQLException> callback) {
        return jdbcSession.withConnection(c -> {
            try {
                return createJdbi(c).withExtension(extensionType, callback);
            } catch (Throwable ex) {
                throw new SQLException(ex);
            }
        });
    }

    public <E> CompletionStage<Void> useExtension(Class<E> extensionType, ExtensionConsumer<E, SQLException> callback) {
        return withExtension(extensionType, e -> {
            callback.useExtension(e);
            return null;
        });
    }

}
