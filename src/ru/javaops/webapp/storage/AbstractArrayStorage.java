package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.exception.StorageException;
import ru.javaops.webapp.model.Resume;

import java.util.Arrays;

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

    /**
     * @return array, contains only Resumes in storage (without null)
     */

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected void doSave(Object key, Resume r) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        storage[insertResume((Integer) key)] = r;
        size++;
    }

    @Override
    protected void doDelete(Object key) {
        size--;
        deleteResume((int) key);
        storage[size] = null;
    }

    @Override
    protected void doUpdate(Object key, Resume r) {
        storage[(int) key] = r;
    }

    @Override
    protected Resume doGet(Object key) {
        return storage[(int) key];
    }

    @Override
    protected Object checkExist(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index >= 0) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    @Override
    protected Object checkNotExist(String uuid) {
        int index = (int) getSearchKey(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    protected abstract int insertResume(int index);

    protected abstract void deleteResume(int index);
}
