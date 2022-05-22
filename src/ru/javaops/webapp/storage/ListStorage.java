package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object getSearchKey(String searchKey) {
        for (Resume r : storage) {
            if (r.getUuid().equals(searchKey)) {
                return storage.indexOf(r);
            }
        }
        return -1;
    }

    @Override
    protected void doSave(Object key, Resume r) {
        storage.add(r);
    }

    @Override
    protected void doDelete(Object key) {
        storage.remove((int) key);
    }

    @Override
    protected void doUpdate(Object key, Resume r) {
        doDelete(key);
        doSave(key, r);
    }

    @Override
    protected Resume doGet(Object key) {
        return storage.get((Integer) key);
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
}
