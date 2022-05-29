package ru.javaops.webapp;

import ru.javaops.webapp.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javaops.webapp.model.ContactType.*;
import static ru.javaops.webapp.model.SectionType.*;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("Медведев Дмитрий");

        resume.setContact(PHONE, "+7(999) 435-23-45");
        resume.setContact(SKYPE, "dimamedvedev");
        resume.setContact(MAIL, "stringer-mdm@yandex.ru");
        resume.setContact(LINKEDIN, "some link");
        resume.setContact(GITHUB, "some link");
        resume.setContact(STACKOVERFLOW, "some link");
        resume.setContact(HOME_PAGE, "some link");

        resume.setSection(OBJECTIVE, new TextSection("Нашальнике ГАДиВСУ"));
        resume.setSection(PERSONAL, new TextSection("Учу Java каждый день"));
        List<String> achievementItems = Arrays.asList("Закончил StartJava за 20 дней", "Мог и быстрее");
        resume.setSection(ACHIEVEMENT, new ListSection(achievementItems));
        List<String> qualificationsItems = Arrays.asList("Инженер", "Java Core");
        resume.setSection(QUALIFICATIONS, new ListSection(qualificationsItems));

        List<Period> periods = Arrays.asList(new Period(LocalDate.of(2015, 7, 30), LocalDate.now(),
                "Нашальнике", "Начальник группы двигателей и ВСУ"));
        Organization organisation = new Organization("ООО НВ Техник", "https://nwtechnic.com/", periods);

        List<Period> periods1 = Arrays.asList(
                new Period(LocalDate.of(2010, 10, 14), LocalDate.of(2013, 4, 1),
                        "Инженер", "Инженер по двигателям и ВСУ"),
                new Period(LocalDate.of(2013, 4, 1), LocalDate.of(2014, 4, 1),
                        "Ведущий инженер", "Ведущий инженер по двигателям и ВСУ"),
                new Period(LocalDate.of(2014, 4, 1), LocalDate.of(2015, 7, 29),
                        "Руководитель группы", "Руководитель группы двигателей Rolls-Royce"));
        Organization organisation1 = new Organization("ОAО АК Трансаеро", "https://www.transaero.ru/", periods1);
        List<Organization> organisations = new ArrayList<>();
        organisations.add(organisation);
        organisations.add(organisation1);
        OrganizationSection experienceSection = new OrganizationSection(organisations);
        resume.setSection(EXPERIENCE, experienceSection);

        List<Period> periods2 = Arrays.asList(
                new Period(LocalDate.of(2022, 4, 24), LocalDate.of(2022, 5, 13),
                        "Студент StartJava", null),
                new Period(LocalDate.of(2022, 4, 24), LocalDate.now(),
                        "Студент BaseJava", null));
        Organization organisation2 = new Organization("JavaOps", "https://javaops.ru/", periods2);
        List<Period> periods3 = Arrays.asList(
                new Period(LocalDate.of(2021, 3, 1), LocalDate.now(), "Студент", null));
        Organization organisation3 = new Organization("JavaRush", "https://javarush.ru/", periods3);
        List<Organization> organisations1 = new ArrayList<>();
        organisations1.add(organisation2);
        organisations1.add(organisation3);
        OrganizationSection educationSection = new OrganizationSection(organisations1);
        resume.setSection(EDUCATION, educationSection);

        System.out.println(resume.getFullName());

        for (ContactType contact : ContactType.values()) {
            System.out.println(contact.getTitle() + " : " + resume.getContact(contact));
        }

        for (SectionType section : SectionType.values()) {
            System.out.println(section.getTitle() + " : " + resume.getSection(section));
        }
    }
}
