package ru.javaops.webapp.web;

import ru.javaops.webapp.Config;
import ru.javaops.webapp.model.*;
import ru.javaops.webapp.storage.Storage;
import ru.javaops.webapp.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.javaops.webapp.model.SectionType.*;
import static ru.javaops.webapp.util.ServletUtil.*;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String uuid = request.getParameter("uuid");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "create":
                r = new Resume();
                for (ContactType contactType : ContactType.values()) {
                    r.getContacts().put(contactType, null);
                }
                for (SectionType sectionType : values()) {
                    r.getSections().put(sectionType, null);
                }
                addEmptyOrganizationSection(r, EXPERIENCE);
                addEmptyOrganizationSection(r, EDUCATION);
                request.setAttribute("experience", r.getSection(EXPERIENCE));
                request.setAttribute("education", r.getSection(EDUCATION));
                break;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                request.setAttribute("experience", r.getSection(EXPERIENCE));
                request.setAttribute("education", r.getSection(EDUCATION));
                break;
            case "edit":
                r = storage.get(uuid);
                addEmptyPeriod(r, EXPERIENCE);
                addEmptyPeriod(r, EDUCATION);
                addEmptyOrganization(r, EXPERIENCE);
                addEmptyOrganization(r, EDUCATION);
                request.setAttribute("experience", r.getSection(EXPERIENCE));
                request.setAttribute("education", r.getSection(EDUCATION));
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r;
        if (uuid.isEmpty()) {
            r = new Resume(fullName);
            insertContacts(r, request);
            insertSections(r, request);
            storage.save(r);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
            insertContacts(r, request);
            insertSections(r, request);
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    private void insertContacts(Resume r, HttpServletRequest request) {
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
    }

    private void insertSections(Resume r, HttpServletRequest request) {
        for (SectionType type : values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> lines = Arrays.stream(value.split("\n"))
                                .filter(s -> !s.trim().isEmpty())
                                .collect(Collectors.toList());
                        r.addSection(type, new ListSection(lines));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = new ArrayList<>();
                        int orgCount = 0;
                        while (true) {
                            String name = request.getParameter(type.name() + "name" + orgCount);
                            if (name == null || name.trim().isEmpty()) {
                                break;
                            }
                            String url = request.getParameter(type.name() + "url" + orgCount);
                            Link homePage = new Link(name, url);
                            List<Organization.Period> periods = new ArrayList<>();
                            int periodCount = 0;
                            while (true) {
                                String title = request.getParameter(type.name() + orgCount + "title" + periodCount);
                                if (title == null || title.trim().isEmpty()) {
                                    break;
                                }
                                String description = request.getParameter(type.name() + orgCount + "description"
                                        + periodCount);
                                description = description.trim().isEmpty() ? null : description;
                                LocalDate start = dateParser(request, type.name() + orgCount + "start"
                                        + periodCount);
                                LocalDate end = dateParser(request, type.name() + orgCount + "end"
                                        + periodCount);
                                end = Objects.equals(end,
                                        LocalDate.now().withDayOfMonth(1)) || end.isAfter(LocalDate.now()) ?
                                        DateUtil.NOW : end;
                                periods.add(new Organization.Period(start, end, title, description));
                                periodCount++;
                            }
                            organizations.add(new Organization(homePage, periods));
                            orgCount++;
                        }
                        r.addSection(type, new OrganizationSection(organizations));
                        break;
                }
            } else {
                r.getSections().remove(type);
            }
        }
    }
}
