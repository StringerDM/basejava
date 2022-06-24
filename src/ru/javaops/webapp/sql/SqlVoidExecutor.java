package ru.javaops.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlVoidExecutor {
    void execute(PreparedStatement ps) throws SQLException;
}
