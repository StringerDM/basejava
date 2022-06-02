package ru.javaops.webapp.storage;

import ru.javaops.webapp.storage.serialisation_strategy.ObjectStreamStrategy;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}