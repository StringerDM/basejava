<%@ page import="ru.javaops.webapp.model.ContactType" %>
<%@ page import="ru.javaops.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javaops.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" required title="Имя должно начинаться с буквы"
                       pattern="[\S][A-Za-zА-Яа-яЁё\s]*" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:choose>
                <c:when test="${type.name().equals('EXPERIENCE')}">
                    <h3>${type.title}</h3>
                    <input type="hidden" name="${type.name()}" value="notNull">
                    <jsp:useBean id="experience" type="ru.javaops.webapp.model.OrganizationSection" scope="request"/>
                    <c:forEach var="organisation" items="${experience.organisations}" varStatus="orgCount">
                        <dl>
                            <dt>Название</dt>
                            <dd><input type="text" name="${type.name()}name${orgCount.index}"
                                       value="${organisation.homePage.name}"></dd>
                        </dl>
                        <dl>
                            <dt>Ссылка</dt>
                            <dd><input type="text" name="${type.name()}url${orgCount.index}"
                                       value="${organisation.homePage.url}"></dd>
                        </dl>
                        <c:forEach var="period" items="${organisation.periods}" varStatus="periodCount">
                            <dl>
                                <dt>Начало, ММ/ГГГГ</dt>
                                <dd><input type="text" name="${type.name()}${orgCount.index}start${periodCount.index}"
                                           value="${period.formattedStart}"></dd>
                                <dt>Окончание, ММ/ГГГГ</dt>
                                <dd><input type="text" name="${type.name()}${orgCount.index}end${periodCount.index}"
                                           value="${period.formattedEnd}"></dd>
                            </dl>
                            <dl>
                                <dt>Заголовок</dt>
                                <dd><input type="text" name="${type.name()}${orgCount.index}title${periodCount.index}"
                                           size=30 value="${period.title}"></dd>
                            </dl>
                            <dl>
                                <dt>Описание</dt>
                                <dd><textarea
                                        name="${type.name()}${orgCount.index}description${periodCount.index}">
                                        ${period.description}
                                </textarea></dd>
                            </dl>
                        </c:forEach>
                    </c:forEach>
                </c:when>
                <c:when test="${type.name().equals('EDUCATION')}">
                    <h3>${type.title}</h3>
                    <input type="hidden" name="${type.name()}" value="notNull">
                    <jsp:useBean id="education" type="ru.javaops.webapp.model.OrganizationSection" scope="request"/>
                    <c:forEach var="organisation" items="${education.organisations}" varStatus="orgCount">
                        <dl>
                            <dt>Название</dt>
                            <dd><input type="text" name="${type.name()}name${orgCount.index}"
                                       value="${organisation.homePage.name}"></dd>
                        </dl>
                        <dl>
                            <dt>Ссылка</dt>
                            <dd><input type="text" name="${type.name()}url${orgCount.index}"
                                       value="${organisation.homePage.url}"></dd>
                        </dl>
                        <c:forEach var="period" items="${organisation.periods}" varStatus="periodCount">
                            <dl>
                                <dt>Начало, ММ/ГГГГ</dt>
                                <dd><input type="text" name="${type.name()}${orgCount.index}start${periodCount.index}"
                                           value="${period.formattedStart}"></dd>
                                <dt>Окончание, ММ/ГГГГ</dt>
                                <dd><input type="text" name="${type.name()}${orgCount.index}end${periodCount.index}"
                                           value="${period.formattedEnd}"></dd>
                            </dl>
                            <dl>
                                <dt>Заголовок</dt>
                                <dd><input type="text" name="${type.name()}${orgCount.index}title${periodCount.index}"
                                           size=30 value="${period.title}"></dd>
                            </dl>
                            <dl>
                                <dt>Описание</dt>
                                <dd><textarea
                                        name="${type.name()}${orgCount.index}description${periodCount.index}">
                                        ${period.description}
                                </textarea></dd>
                            </dl>
                        </c:forEach>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><textarea name="${type.name()}">${resume.getSection(type)}</textarea></dd>
                    </dl>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>