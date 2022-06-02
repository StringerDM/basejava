package ru.javaops.webapp.storage;

import ru.javaops.webapp.storage.serialisation_strategy.ObjectStreamStrategy;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamStrategy()) {
        });
    }
}