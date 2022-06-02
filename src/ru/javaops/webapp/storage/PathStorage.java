package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.StorageException;
import ru.javaops.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private SerialisationStrategy serialisationStrategy;

    protected PathStorage(String dir, SerialisationStrategy serialisationStrategy) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
        this.serialisationStrategy = serialisationStrategy;
    }

    public void setSerialisationStrategy(SerialisationStrategy serialisationStrategy) {
        this.serialisationStrategy = serialisationStrategy;
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        int size;
        try {
            size = (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return size;
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(String.valueOf(directory), uuid);
    }

    @Override
    protected void doUpdate(Path path, Resume r) {
        try {
            serialisationStrategy.serialize(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Path path, Resume r) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create Path " + path.toAbsolutePath(), path.getFileName().toString(), e);
        }
        doUpdate(path, r);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serialisationStrategy.deserialize(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.getFileName().toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> paths;
        try {
            paths = Files.list(directory).map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Directory read error", null);
        }
        return paths;
    }
}
