package ru.javaops.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlTransaction {
    void execute(Connection conn) throws SQLException;
}
