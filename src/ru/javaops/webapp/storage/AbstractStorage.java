package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public final void update(Resume r) {
        doUpdate(getNotExistKey(r.getUuid()), r);
    }

    public final void save(Resume r) {
        doSave(getExistKey(r.getUuid()), r);
    }

    public final Resume get(String uuid) {
        return doGet(getNotExistKey(uuid));
    }

    public final void delete(String uuid) {
        doDelete(getNotExistKey(uuid));
    }

    protected abstract Object getSearchKey(String searchKey);

    protected abstract void doSave(Object key, Resume r);

    protected abstract void doDelete(Object key);

    protected abstract void doUpdate(Object key, Resume r);

    protected abstract Resume doGet(Object key);

    protected Object getExistKey(String uuid) {
        Object key = getSearchKey(uuid);
        if (isExist(key)) {
            throw new ExistStorageException(uuid);
        }
        return key;
    }

    protected Object getNotExistKey(String uuid) {
        Object key = getSearchKey(uuid);
        if (!isExist(key)) {
            throw new NotExistStorageException(uuid);
        }
        return key;
    }

    protected abstract boolean isExist(Object key);

}
