package ru.javaops.webapp.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.webapp.Config;
import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static ru.javaops.webapp.ResumeTestData.createTestResume;
import static ru.javaops.webapp.model.ContactType.PHONE;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    private static final String UUID_1 = "70db7010-8e01-414b-b725-c9f32adeb709";
    private static final String UUID_2 = "229044ce-5078-464c-858a-7ea9ecb6808c";
    private static final String UUID_3 = "608453a8-65c0-48a3-b2c2-1093ab925706";
    private static final String UUID_4 = UUID.randomUUID().toString();
    private static final String UUID_NOT_EXIST = "dummy";
    private static final Resume RESUME_1 = createTestResume(UUID_1, "Соловьев Александр");
    private static final Resume RESUME_2 = createTestResume(UUID_2, "Федотова Полина");
    private static final Resume RESUME_3 = createTestResume(UUID_3, "Федотова Полина");
    private static final Resume RESUME_4 = createTestResume(UUID_4, "Романова Кира");

    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_2);
        storage.save(RESUME_1);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() {
        Resume r = new Resume(UUID_1, "Ворона Александр");
        r.setContacts(RESUME_1.getContacts());
        r.setSections(RESUME_1.getSections());
        r.addContact(PHONE, "+7(999) 999-99-99");
        storage.update(r);
        Assert.assertEquals(r, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume(UUID_NOT_EXIST));
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_3);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    public void assertGet(Resume r) {
        Assert.assertEquals(r, storage.get(r.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_3);
        assertSize(2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXIST);
    }

    @Test
    public void getAllSorted() {
        List<Resume> resumes = storage.getAllSorted();
        assertSize(resumes.size());
        List<Resume> expectedResumes = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);
        Assert.assertEquals(expectedResumes, resumes);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    public void assertSize(int expectSize) {
        Assert.assertEquals(expectSize, storage.size());
    }
}