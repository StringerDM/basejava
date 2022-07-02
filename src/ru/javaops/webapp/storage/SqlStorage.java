package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.*;
import ru.javaops.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        Resume[] resume = new Resume[1];
        sqlHelper.transactionalExecute(conn -> {
            sqlHelper.execute("SELECT * FROM resume r WHERE r.uuid=?", conn, ps -> {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume[0] = (new Resume(rs.getString("uuid"), rs.getString("full_name")));
            });
            sqlHelper.executeWhile(uuid, "SELECT * FROM contact c WHERE c.resume_uuid=?", conn, rs ->
                    addContact(resume[0], rs));
            sqlHelper.executeWhile(uuid, "SELECT * FROM section s WHERE s.resume_uuid=?", conn, rs ->
                    addSection(resume[0], rs));
        });
        return resume[0];
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
        sqlHelper.execute("UPDATE resume SET full_name =? WHERE uuid =?", conn,  ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
        });
            deleteFromTable(r.getUuid(), "contact");
            deleteFromTable(r.getUuid(), "section");
            saveContacts(r, conn);
            saveSections(r, conn);
        });
    }

    private void deleteFromTable(String uuid, String table) {
        sqlHelper.execute(String.format("DELETE FROM %s WHERE resume_uuid=?", table), ps -> {
            ps.setString(1, uuid);
            ps.execute();
            return null;
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
            saveSections(r, conn);
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
        Map<String, Resume> resumeMap = new LinkedHashMap<>();
        sqlHelper.transactionalExecute(conn -> {
            sqlHelper.executeWhile(null, "SELECT * FROM resume r ORDER BY r.full_name, r.uuid", conn, rs -> {
                Resume r = (new Resume(rs.getString("uuid"), rs.getString("full_name")));
                resumeMap.put(r.getUuid(), r);
            });
            sqlHelper.executeWhile(null, "SELECT * FROM contact", conn, rs -> {
                Resume r = resumeMap.get(rs.getString("resume_uuid"));
                addContact(r, rs);
            });
            sqlHelper.executeWhile(null, "SELECT * FROM section", conn, rs -> {
                Resume r = resumeMap.get(rs.getString("resume_uuid"));
                addSection(r, rs);
            });
        });
        return new ArrayList<>(resumeMap.values());
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
            ps.setString(2, r.getUuid());
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getValue());
                ps.setString(3, e.getKey().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (value, resume_uuid, type) VALUES (?,?,?)")) {
            ps.setString(2, r.getUuid());
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
                ps.setString(3, e.getKey().name());
                switch (e.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        ps.setString(1, ((TextSection) e.getValue()).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String s : ((ListSection) e.getValue()).getStrings()) {
                            stringBuilder.append(s).append("\n");
                        }
                        ps.setString(1, stringBuilder.toString());
                        break;
                }
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

    private void addSection(Resume r, ResultSet rs) throws SQLException {
        String value = rs.getString("value");
        SectionType section = SectionType.valueOf(rs.getString("type"));
        if (value != null) {
            switch (section) {
                case OBJECTIVE:
                case PERSONAL:
                    r.addSection(section, new TextSection(value));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    r.addSection(section, new ListSection(value.split("\n")));
                    break;
            }
        }
    }
}
