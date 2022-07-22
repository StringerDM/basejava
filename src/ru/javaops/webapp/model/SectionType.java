package ru.javaops.webapp.model;

import ru.javaops.webapp.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public enum SectionType {
    OBJECTIVE("Позиция"),
    PERSONAL("Личные качества"),
    ACHIEVEMENT("Достижения") {
        @Override
        protected <T> String toHtml0(T section) {
            return fromListSection(section);
        }
    },
    QUALIFICATIONS("Квалификация") {
        @Override
        protected <T> String toHtml0(T section) {
            return fromListSection(section);
        }
    },
    EXPERIENCE("Опыт работы") {
        @Override
        protected <T> String toHtml0(T section) {
            return fromOrganisationSection(section);
        }
    },
    EDUCATION("Образование") {
        @Override
        protected <T> String toHtml0(T section) {
            return fromOrganisationSection(section);
        }
    };

    private final String title;

    SectionType(String value) {
        this.title = value;
    }

    public static String toLink(String href, String title) {
        if (href == null) {
            return "<h4>" + title + "</h4>";
        }
        return "<h4><a href='" + href + "'>" + title + "</a></h4>";
    }

    public String getTitle() {
        return title;
    }

    public <T extends AbstractSection> String toHtml(T section) {
        return (section == null) ? "" : toHtml0(section);
    }

    protected <T> String toHtml0(T section) {
        String value = ((TextSection) section).getContent();
        return "<h2>" + getTitle() + "</h2>\n" + value;
    }

    protected <T> String fromListSection(T section) {
        List<String> value = ((ListSection) section).getStrings();
        StringBuilder strings = new StringBuilder();
        for (String s : value) {
            strings.append(s).append("<br/>");
        }
        return "<h2>" + getTitle() + "</h2>\n" + strings;
    }

    protected <T> String fromOrganisationSection(T section) {
        StringBuilder strings = new StringBuilder();
        strings.append("<h2>").append(getTitle()).append("</h2>\n");
        List<Organization> organizations = ((OrganizationSection) section).getOrganisations();
        for (Organization org : organizations) {
            Link homePage = org.getHomePage();
            strings.append("<h3 style=\"text-align: left; padding-left: 80px;\"><a href=\"").append(homePage.getUrl())
                    .append("\">").append(homePage.getName()).append("</a></h3>");

            List<Organization.Period> periods = org.getPeriods();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            for (Organization.Period period : periods) {
                strings.append("<p style=\"padding-left: 80px;\">");
                String dates = period.getStart().format(formatter) + " - " +
                        ((period.getEnd().isEqual(LocalDate.now()) || period.getEnd().isAfter(LocalDate.now())) ? "сейчас" :
                                period.getEnd().format(formatter));
                strings.append(dates).append("<br/>").append(period.getTitle()).append("</p>");
                if (period.getDescription() != null) {
                    strings.append("<p style=\"padding-left: 80px;\">").append(period.getDescription()).append("</p>");
                }
            }
        }
        return strings.toString();
    }
}
