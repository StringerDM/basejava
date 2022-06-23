package ru.javaops.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlExecutor<T> {

    T execute(PreparedStatement ps) throws SQLException;
}
