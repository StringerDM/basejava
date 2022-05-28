package ru.javaops.webapp;

import ru.javaops.webapp.model.*;

import java.time.LocalDate;

import static ru.javaops.webapp.model.ContactType.*;
import static ru.javaops.webapp.model.SectionType.*;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("Медведев Дмитрий");

        resume.contacts.put(TELEPHONE, "+7(999) 435-23-45");
        resume.contacts.put(SKYPE, "dimamedvedev");
        resume.contacts.put(EMAIL, "stringer-mdm@yandex.ru");
        resume.contacts.put(LINKEDIN, "some link");
        resume.contacts.put(GITHUB, "some link");
        resume.contacts.put(STACKOVERFLOW, "some link");
        resume.contacts.put(HOMEPAGE, "some link");

        resume.sections.put(OBJECTIVE, new TextSection("Нашальнике ГАДиВСУ"));
        resume.sections.put(PERSONAL, new TextSection("Учу Java каждый день"));
        resume.sections.put(ACHIEVEMENT, new ListSection("Закончил StartJava за 20 дней", "Мог и быстрее"));
        resume.sections.put(QUALIFICATIONS, new ListSection("Инженер", "Java Core"));

        Organisation organisation = new Organisation("ООО НВ Техник", "https://nwtechnic.com/",
                new Period(LocalDate.of(2015, 7, 30), LocalDate.now(), "Нашальнике",
                        "Начальник группы двигателей и ВСУ"));
        Organisation organisation1 = new Organisation("ОAО АК Трансаеро", "https://www.transaero.ru/",
                new Period(LocalDate.of(2010, 10, 14), LocalDate.of(2013, 4, 1),
                        "Инженер", "Инженер по двигателям и ВСУ"),
                new Period(LocalDate.of(2013, 4, 1), LocalDate.of(2014, 4, 1),
                        "Ведущий инженер", "Ведущий инженер по двигателям и ВСУ"),
                new Period(LocalDate.of(2014, 4, 1), LocalDate.of(2015, 7, 29),
                        "Руководитель группы", "Руководитель группы двигателей Rolls-Royce"));
        OrganisationSection experienceSection = new OrganisationSection();
        experienceSection.organisations.add(organisation);
        experienceSection.organisations.add(organisation1);
        resume.sections.put(EXPERIENCE, experienceSection);

        OrganisationSection educationSection = new OrganisationSection();
        Organisation organisation2 = new Organisation("JavaOps", "https://javaops.ru/",
                new Period(LocalDate.of(2022, 4, 24), LocalDate.of(2022, 5, 13),
                        "Студент StartJava", null),
                new Period(LocalDate.of(2022, 4, 24), LocalDate.now(),
                        "Студент BaseJava", null));
        Organisation organisation3 = new Organisation("JavaRush", "https://javarush.ru/",
                new Period(LocalDate.of(2021, 3, 1), LocalDate.now(), "Студент", null));
        educationSection.organisations.add(organisation2);
        educationSection.organisations.add(organisation3);
        resume.sections.put(EDUCATION, educationSection);

        for (ContactType contact : ContactType.values()) {
            System.out.println(contact.getTitle() + " : " + resume.contacts.get(contact));
        }

        for (SectionType section : SectionType.values()) {
            System.out.println("\n" + section.getTitle() + "\n" + resume.sections.get(section));
        }
    }
}
