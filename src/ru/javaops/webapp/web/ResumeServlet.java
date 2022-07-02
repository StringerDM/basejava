package ru.javaops.webapp.web;

import ru.javaops.webapp.Config;
import ru.javaops.webapp.model.Resume;
import ru.javaops.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.javaops.webapp.ResumeTestData.createTestResume;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getStorage();
        storage.clear();
        storage.save(createTestResume("70db7010-8e01-414b-b725-c9f32adeb709", "Соловьев Александр"));
        storage.save(createTestResume("229044ce-5078-464c-858a-7ea9ecb6808c", "Федотова Полина"));
        storage.save(createTestResume("608453a8-65c0-48a3-b2c2-1093ab925706", "Романова Кира"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String name = request.getParameter("name");
        response.getWriter().write(name == null ? "Hello Resumes!" : "Hello " + name + '!');

        response.getWriter().write("<table><tr><th>uuid</th><th>fullName</th>");
        List<Resume> resumes = storage.getAllSorted();
        for (Resume r : resumes) {
            response.getWriter().write("<tr><td>" + r.getUuid() + "</td><td>" + r.getFullName() + "</td></tr>");
        }
        response.getWriter().write("</table>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
