package ru.javaops.webapp.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javaops.webapp.exception.StorageException;
import ru.javaops.webapp.model.Resume;

import static ru.javaops.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveStorageOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (Exception e) {
            Assert.fail("Not expected storage overflow");
        }
        storage.save(new Resume());
    }
}