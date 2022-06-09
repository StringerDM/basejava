package ru.javaops.webapp.storage.serialisation_strategy;

import ru.javaops.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@FunctionalInterface
interface CustomWriter<T> {

    void write(T t) throws IOException;
}

@FunctionalInterface
interface CustomListWriter<T> {

    void write(List<T> tList) throws IOException;
}

public class DataStreamSerializer implements SerialisationStrategy {

    @Override
    public void serialize(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeWithException(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            writeWithException(dos, r.getSections().entrySet(), entry -> {
                SectionType section = entry.getKey();
                dos.writeUTF(section.name());
                switch (section) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(((TextSection) entry.getValue()).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> strings = ((ListSection) entry.getValue()).getStrings();
                        writeWithException(dos, strings, dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeWithException(dos, ((OrganizationSection) entry.getValue()).getOrganisations(), org -> {
                            dos.writeUTF(org.getHomePage().getName());
                            String url = org.getHomePage().getUrl() != null ? org.getHomePage().getUrl() : "null";
                            dos.writeUTF(url);
                            writeWithException(dos, org.getPeriods(), p -> {
                                dos.writeUTF(p.getStart().toString());
                                dos.writeUTF(p.getEnd().toString());
                                dos.writeUTF(p.getTitle());
                                String description = p.getDescription() != null ? p.getDescription() : "null";
                                dos.writeUTF(description);
                            });
                        });
                }
            });
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            readWithException(dis, resume, r -> r.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readWithException(dis, resume, r -> {
                SectionType section = SectionType.valueOf(dis.readUTF());
                switch (section) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSection(section, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(section, new ListSection(
                                readToListWithException(dis, new ArrayList<>(), l -> l.add(dis.readUTF()))));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        resume.addSection(section, new OrganizationSection(readToListWithException(dis, new ArrayList<>(),
                                organizations -> {
                                    String name = dis.readUTF();
                                    String url = dis.readUTF();
                                    Organization organization = new Organization(name, url.equals("null") ? null : url,
                                            readToListWithException(dis, new ArrayList<>(), periods -> {
                                                LocalDate start = LocalDate.parse(dis.readUTF());
                                                LocalDate end = LocalDate.parse(dis.readUTF());
                                                String title = dis.readUTF();
                                                String description = dis.readUTF();
                                                periods.add(new Organization.Period(start, end, title,
                                                        description.equals("null") ? null : description));
                                            }));
                                    organizations.add(organization);
                                })));
                        break;
                }
            });
            return resume;
        }
    }

    private <T> void writeWithException(DataOutputStream dos, Collection<T> collection, CustomWriter<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T element : collection) {
            writer.write(element);
        }
    }

    private <T> void readWithException(DataInputStream dis, T t, CustomWriter<T> customWriter) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            customWriter.write(t);
        }
    }

    private <T> List<T> readToListWithException(DataInputStream dis, List<T> tList, CustomListWriter<T> listWriter) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            listWriter.write(tList);
        }
        return tList;
    }
}