package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.StorageException;
import ru.javaops.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        storage[insertResume((Integer) searchKey)] = r;
        size++;
    }

    @Override
    protected void doDelete(Object searchKey) {
        size--;
        deleteResume((int) searchKey);
        storage[size] = null;
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        storage[(int) searchKey] = r;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected List<Resume> getList() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    protected abstract int insertResume(int index);

    protected abstract void deleteResume(int index);

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }
}
