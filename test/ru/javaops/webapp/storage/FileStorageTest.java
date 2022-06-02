package ru.javaops.webapp.storage;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}