package ru.javaops.webapp.storage.serialisation_strategy;

import ru.javaops.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements SerialisationStrategy {

    @Override
    public void serialize(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }
            Map<SectionType, AbstractSection> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                AbstractSection section = entry.getValue();
                if (section instanceof TextSection) {
                    TextSection textSection = ((TextSection) section);
                    dos.writeUTF(textSection.getClass().getSimpleName());
                    dos.writeUTF(textSection.getContent());
                } else if (section instanceof ListSection) {
                    ListSection listSection = ((ListSection) section);
                    dos.writeUTF(listSection.getClass().getSimpleName());
                    List<String> strings = listSection.getStrings();
                    dos.writeInt(strings.size());
                    for (String s : strings) {
                        dos.writeUTF(s);
                    }
                } else if (section instanceof OrganizationSection) {
                    OrganizationSection organizationSection = ((OrganizationSection) section);
                    dos.writeUTF(organizationSection.getClass().getSimpleName());
                    List<Organization> organizations = organizationSection.getOrganisations();
                    dos.writeInt(organizations.size());
                    writeOrganizationSection(organizations, dos);
                }
            }
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                String section = dis.readUTF();
                if (section.equals("TextSection")) {
                    resume.addSection(sectionType, new TextSection(dis.readUTF()));
                } else if (section.equals("ListSection")) {
                    ListSection listSection = new ListSection();
                    int size1 = dis.readInt();
                    for (int j = 0; j < size1; j++) {
                        listSection.addString(dis.readUTF());
                    }
                    resume.addSection(sectionType, listSection);
                } else if (section.equals("OrganizationSection")) {
                    size = dis.readInt();
                    resume.addSection(sectionType, readOrganizationSection(size, dis));
                }
            }
            return resume;
        }
    }

    private void writeOrganizationSection(List<Organization> organizations, DataOutputStream dos) throws IOException {
        for (Organization org : organizations) {
            dos.writeUTF(org.getHomePage().getName());
            dos.writeUTF(org.getHomePage().getUrl());
            List<Organization.Period> periods = org.getPeriods();
            dos.writeInt(periods.size());
            for (Organization.Period period : org.getPeriods()) {
                dos.writeUTF(period.getStart().toString());
                dos.writeUTF(period.getEnd().toString());
                dos.writeUTF(period.getTitle());
                if (period.getDescription() != null){
                    dos.writeUTF(period.getDescription());
                } else {
                    dos.writeUTF("null");
                }
            }
        }
    }

    private OrganizationSection readOrganizationSection(int size, DataInputStream dis) throws IOException {
        OrganizationSection organizationSection = new OrganizationSection();
        for (int i = 0; i < size; i++) {
            String name = dis.readUTF();
            String url = dis.readUTF();
            Organization organization = new Organization(name, url, new ArrayList<>());
            int periodsSize = dis.readInt();
            for (int j = 0; j < periodsSize; j++) {
                LocalDate start = LocalDate.parse(dis.readUTF());
                LocalDate end = LocalDate.parse(dis.readUTF());
                String title = dis.readUTF();
                String description = dis.readUTF();
                if (description.equals("null")) {
                    description = null;
                }
                organization.addPeriod(start, end, title, description);
            }
            organizationSection.addOrganisation(organization);
        }
        return organizationSection;
    }
}