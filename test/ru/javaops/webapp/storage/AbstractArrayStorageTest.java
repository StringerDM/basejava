package ru.javaops.webapp.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.webapp.exception.*;
import ru.javaops.webapp.model.Resume;

import java.lang.reflect.Field;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() throws NoSuchFieldException, IllegalAccessException {
        storage.clear();
        Assert.assertEquals(0, storage.size());

        Field field = storage.getClass().getSuperclass().getDeclaredField("storage");
        field.setAccessible(true);
        Resume[] resumes = (Resume[]) field.get(storage);
        for (int i = 0; i < 3; i++) {
            Assert.assertNull(resumes[i]);
        }
    }

    @Test
    public void update() {
        Resume r = new Resume(UUID_1);
        storage.update(r);
        Assert.assertSame(r, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume("dummy"));
    }

    @Test
    public void save() {
        Resume r = new Resume("UUID_4");
        storage.save(r);
        Assert.assertEquals(4, storage.size());
        Assert.assertSame(r, storage.get("UUID_4"));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(new Resume(UUID_3));
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        try {
            for (int i = storage.size(); i < 10000; i++) {
                Resume r = new Resume();
                storage.save(r);
            }
        } catch (Exception e) {
            Assert.fail("Not expected storage overflow");
        }
        Resume r1 = new Resume();
        storage.save(r1);
    }

    @Test
    public void get() {
        Resume resume = new Resume(UUID_2);
        Assert.assertEquals(resume, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws IllegalAccessException, NoSuchFieldException {
        storage.delete(UUID_3);
        Assert.assertEquals(2, storage.size());

        Field field = storage.getClass().getSuperclass().getDeclaredField("storage");
        field.setAccessible(true);
        Resume[] resumes = (Resume[]) field.get(storage);
        Assert.assertNull(resumes[2]);

        storage.get(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertEquals(3, resumes.length);
        Assert.assertEquals(resumes[0], storage.get(resumes[0].getUuid()));
        Assert.assertEquals(resumes[1], storage.get(resumes[1].getUuid()));
        Assert.assertEquals(resumes[2], storage.get(resumes[2].getUuid()));
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }
}