package ru.javaops.webapp.storage;

import org.junit.Test;
import ru.javaops.webapp.Config;
import ru.javaops.webapp.exception.StorageException;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(new SqlStorage(Config.get().getDbUrl(), Config.get().getDbUser(), Config.get().getDbPassword()));
    }

    @Override
    @Test(expected = StorageException.class)
    public void saveExist() {
        storage.save(RESUME_3);
    }
}