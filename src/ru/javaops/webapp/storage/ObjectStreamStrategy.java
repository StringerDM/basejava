package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.StorageException;
import ru.javaops.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStrategy implements SerialisationStrategy {

    @Override
    public void serialize(Resume r, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(r);
        }
    }

    @Override
    public Resume deserialize(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
