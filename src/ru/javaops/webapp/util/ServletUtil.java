package ru.javaops.webapp.util;

import ru.javaops.webapp.model.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class ServletUtil {

    public static void addEmptyOrganizationSection(Resume r, SectionType sectionType) {
        r.addSection(sectionType, new OrganizationSection());
        addEmptyOrganization(r, sectionType);
    }

    public static void addEmptyOrganization(Resume r, SectionType sectionType) {
        Link link = new Link();
        Organization.Period period = new Organization.Period();
        Organization org = new Organization(link, new ArrayList<>(Collections.singletonList(period)));
        ((OrganizationSection) r.getSection(sectionType)).addOrganisation(org);
    }

    public static void addEmptyPeriod(Resume r, SectionType sectionType) {
        for (Organization org : ((OrganizationSection) r.getSection(sectionType)).getOrganisations()) {
            org.addPeriod(new Organization.Period(null, null, "", ""));
        }
    }

    public static LocalDate dateParser(HttpServletRequest request, String param) {
        return LocalDate.parse("01/" + request.getParameter(param), DateUtil.DATE_FORMATTER);
    }
}
