package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerialisationStrategy {

    void serialize(Resume r, OutputStream os) throws IOException;
    Resume deserialize(InputStream is) throws IOException;

}
