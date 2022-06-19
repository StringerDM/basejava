package ru.javaops.webapp.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface BlockOfCode<T> {

    T execute(PreparedStatement ps) throws SQLException;
}
