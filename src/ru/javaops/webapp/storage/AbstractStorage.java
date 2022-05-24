package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public final void update(Resume r) {
        doUpdate(getExistKey(r.getUuid()), r);
    }

    public final void save(Resume r) {
        doSave(getNotExistKey(r.getUuid()), r);
    }

    public final Resume get(String uuid) {
        return doGet(getExistKey(uuid));
    }

    public final void delete(String uuid) {
        doDelete(getExistKey(uuid));
    }

    protected abstract Object getSearchKey(String searchKey);

    protected abstract void doSave(Object searchKey, Resume r);

    protected abstract void doDelete(Object searchKey);

    protected abstract void doUpdate(Object searchKey, Resume r);

    protected abstract Resume doGet(Object searchKey);

    protected Object getNotExistKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected Object getExistKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract boolean isExist(Object searchKey);

}
