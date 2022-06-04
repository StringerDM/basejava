package ru.javaops.webapp.storage;

import ru.javaops.webapp.storage.serialisation_strategy.JsonStreamStrategy;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamStrategy()));
    }
}