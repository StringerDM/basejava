package ru.javaops.webapp.storage;

import ru.javaops.webapp.storage.serialisation_strategy.XmlStreamStrategy;

public class XmlPathStorageTest extends AbstractStorageTest {

    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamStrategy()));
    }
}
