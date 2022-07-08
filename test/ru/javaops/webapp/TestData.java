package ru.javaops.webapp;

import ru.javaops.webapp.model.Resume;

import java.util.UUID;

import static ru.javaops.webapp.ResumeTestData.createTestResume;

public class TestData {
    public static final String UUID_1 = "70db7010-8e01-414b-b725-c9f32adeb709";
    public static final String UUID_2 = "229044ce-5078-464c-858a-7ea9ecb6808c";
    public static final String UUID_3 = "608453a8-65c0-48a3-b2c2-1093ab925706";
    public static final String UUID_4 = UUID.randomUUID().toString();
    public static final String UUID_NOT_EXIST = "dummy";
    public static final Resume RESUME_1 = createTestResume(UUID_1, "Соловьев Александр");
    public static final Resume RESUME_2 = createTestResume(UUID_2, "Федотова Полина");
    public static final Resume RESUME_3 = createTestResume(UUID_3, "Федотова Полина");
    public static final Resume RESUME_4 = createTestResume(UUID_4, "Романова Кира");

}
