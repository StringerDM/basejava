package ru.javaops.webapp.storage;

import ru.javaops.webapp.storage.serialisation_strategy.ObjectStreamStrategy;

public class ObjectPathStorageTest extends AbstractStorageTest {

    public ObjectPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamStrategy()) {
        });
    }
}