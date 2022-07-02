package ru.javaops.webapp.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlResultSetExecutor {
    void execute(ResultSet rs) throws SQLException;
}
