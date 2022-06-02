package ru.javaops.webapp.storage;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamStrategy()) {
        });
    }
}