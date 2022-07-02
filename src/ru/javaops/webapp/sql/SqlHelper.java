package ru.javaops.webapp.sql;

import ru.javaops.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlHelper {

    public final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void execute(String statement) {
        execute(statement, PreparedStatement::execute);
    }

    public <T> T execute(String statement, SqlExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    public void executeWhile(String uuid, String statement, Connection conn, SqlResultSetExecutor executor) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            if (uuid != null) {
                ps.setString(1, uuid);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                executor.execute(rs);
            }
        }
    }

    public void execute(String statement, Connection conn, SqlVoidExecutor executor) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            executor.execute(ps);
        }
    }

    public void transactionalExecute(SqlTransaction executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                executor.execute(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
