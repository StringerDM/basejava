package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.AbstractSection;
import ru.javaops.webapp.model.ContactType;
import ru.javaops.webapp.model.Resume;
import ru.javaops.webapp.model.SectionType;
import ru.javaops.webapp.sql.SqlHelper;
import ru.javaops.webapp.util.JsonParser;

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
        return sqlHelper.transactionExecute(conn -> {
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid=?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = (new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact c WHERE c.resume_uuid=?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContact(resume, rs);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section s WHERE s.resume_uuid=?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(resume, rs);
                }
            }
            return resume;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name =? WHERE uuid =?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteFromTable(r.getUuid(), "contact", conn);
            deleteFromTable(r.getUuid(), "section", conn);
            saveContacts(r, conn);
            saveSections(r, conn);
            return null;
        });
    }

    private void deleteFromTable(String uuid, String table, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE resume_uuid=?", table))) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            saveContacts(r, conn);
            saveSections(r, conn);
            return null;
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
        return sqlHelper.transactionExecute(conn -> {
            Map<String, Resume> resumeMap = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY r.full_name, r.uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = (new Resume(rs.getString("uuid"), rs.getString("full_name")));
                    resumeMap.put(r.getUuid(), r);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumeMap.get(rs.getString("resume_uuid"));
                    addContact(r, rs);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumeMap.get(rs.getString("resume_uuid"));
                    addSection(r, rs);
                }
            }
            return new ArrayList<>(resumeMap.values());
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
                AbstractSection section = e.getValue();
                ps.setString(1, JsonParser.write(section, AbstractSection.class));

//                switch (e.getKey()) {
//                    case OBJECTIVE:
//                    case PERSONAL:
//                        ps.setString(1, ((TextSection) e.getValue()).getContent());
//                        break;
//                    case ACHIEVEMENT:
//                    case QUALIFICATIONS:
//                        StringBuilder stringBuilder = new StringBuilder();
//                        for (String s : ((ListSection) e.getValue()).getStrings()) {
//                            stringBuilder.append(s).append("\n");
//                        }
//                        ps.setString(1, stringBuilder.toString());
//                        break;
//                }
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
        if (value != null) {
            SectionType section = SectionType.valueOf(rs.getString("type"));
            r.addSection(section, JsonParser.read(value, AbstractSection.class));
//
//
//            switch (section) {
//                case OBJECTIVE:
//                case PERSONAL:
//                    r.addSection(section, new TextSection(value));
//                    break;
//                case ACHIEVEMENT:
//                case QUALIFICATIONS:
//                    r.addSection(section, new ListSection(value.split("\n")));
//                    break;
        }
    }
}

