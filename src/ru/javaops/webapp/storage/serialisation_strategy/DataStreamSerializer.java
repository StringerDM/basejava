package ru.javaops.webapp.storage.serialisation_strategy;

import ru.javaops.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@FunctionalInterface
interface CustomConsumer<T> {
    void accept(T t, DataOutputStream dos) throws IOException;

}

@FunctionalInterface
interface CustomBiConsumer<K, V> {
    void accept(K k, V v, DataOutputStream dos) throws IOException;

}

public class DataStreamSerializer implements SerialisationStrategy {

    @Override
    public void serialize(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            writeWithException(contacts, dos, (k, v, d) -> {
                d.writeUTF(k.name());
                d.writeUTF(v);
            });
            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());
            writeWithException(sections, dos, (k, v, d) -> {
                d.writeUTF(k.name());
                String section = v.getClass().getSimpleName();
                d.writeUTF(section);
                switch (section) {
                    case "TextSection":
                        d.writeUTF(((TextSection) v).getContent());
                        break;
                    case "ListSection":
                        List<String> strings = ((ListSection) v).getStrings();
                        d.writeInt(strings.size());
                        writeWithException(strings, d, (s, d1) -> d1.writeUTF(s));
                        break;
                    case "OrganizationSection":
                        List<Organization> organizations = ((OrganizationSection) v).getOrganisations();
                        d.writeInt(organizations.size());
                        writeOrganizationSection(organizations, d);
                }
            });
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                String section = dis.readUTF();
                switch (section) {
                    case "TextSection":
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    case "ListSection":
                        ListSection listSection = new ListSection();
                        int listSize = dis.readInt();
                        for (int j = 0; j < listSize; j++) {
                            listSection.addString(dis.readUTF());
                        }
                        resume.addSection(sectionType, listSection);
                        break;
                    case "OrganizationSection":
                        resume.addSection(sectionType, readOrganizationSection(dis.readInt(), dis));
                        break;
                }
            }
            return resume;
        }
    }

    private void writeOrganizationSection(List<Organization> organizations, DataOutputStream dos) throws IOException {
        writeWithException(organizations, dos, (org, d) -> {
            d.writeUTF(org.getHomePage().getName());
            String url = org.getHomePage().getUrl() != null ? org.getHomePage().getUrl() : "null";
            d.writeUTF(url);
            List<Organization.Period> periods = org.getPeriods();
            d.writeInt(periods.size());
            writeWithException(org.getPeriods(), d, (p, d1) -> {
                d1.writeUTF(p.getStart().toString());
                d1.writeUTF(p.getEnd().toString());
                d1.writeUTF(p.getTitle());
                String description = p.getDescription() != null ? p.getDescription() : "null";
                d1.writeUTF(description);
            });
        });
    }

    private OrganizationSection readOrganizationSection(int size, DataInputStream dis) throws IOException {
        OrganizationSection organizationSection = new OrganizationSection();
        for (int i = 0; i < size; i++) {
            String name = dis.readUTF();
            String url = dis.readUTF();
            url = url.equals("null") ? null : url;
            Organization organization = new Organization(name, url, new ArrayList<>());
            int periodsSize = dis.readInt();
            for (int j = 0; j < periodsSize; j++) {
                LocalDate start = LocalDate.parse(dis.readUTF());
                LocalDate end = LocalDate.parse(dis.readUTF());
                String title = dis.readUTF();
                String description = dis.readUTF();
                description = description.equals("null") ? null : description;
                organization.addPeriod(start, end, title, description);
            }
            organizationSection.addOrganisation(organization);
        }
        return organizationSection;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, CustomConsumer<T> consumer) throws IOException {
        Objects.requireNonNull(collection);
        for (T t : collection) {
            consumer.accept(t, dos);
        }
    }

    private <K, V> void writeWithException(Map<K, V> map, DataOutputStream dos, CustomBiConsumer<K, V> biConsumer) throws IOException {
        Objects.requireNonNull(map);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }
            biConsumer.accept(k, v, dos);
        }
    }
}