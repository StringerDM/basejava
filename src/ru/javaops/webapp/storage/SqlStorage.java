package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.ContactType;
import ru.javaops.webapp.model.Resume;
import ru.javaops.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "     WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContact(r, rs);
                    } while (rs.next());
                    return r;
                });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            sqlHelper.execute("UPDATE resume SET full_name =? WHERE uuid =?", conn, ps -> {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            });
            sqlHelper.execute("DELETE FROM contact WHERE resume_uuid=?", ps -> {
                ps.setString(1, r.getUuid());
                ps.execute();
                return null;
            });
            saveContacts(r, conn);
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            sqlHelper.execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", conn, ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            });
            saveContacts(r, conn);
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            if (!ps.execute()) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "  ORDER BY r.full_name, r.uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    boolean hasNext = rs.next();
                    while (hasNext) {
                        Resume r = (new Resume(rs.getString("uuid"), rs.getString("full_name")));
                        resumes.add(r);
                        do {
                            addContact(r, rs);
                        } while ((hasNext = rs.next()) && Objects.equals(rs.getString("uuid"), r.getUuid()));
                    }
                    return resumes;
                });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void saveContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, resume_uuid, type) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getValue());
                ps.setString(2, r.getUuid());
                ps.setString(3, e.getKey().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addContact(Resume r, ResultSet rs) throws SQLException {
        String value = rs.getString("value");
        ContactType type = ContactType.valueOf(rs.getString("type"));
        if (value != null) {
            r.addContact(type, value);
        }
    }
}
