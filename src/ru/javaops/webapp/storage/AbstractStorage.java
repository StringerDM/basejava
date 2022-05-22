package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public final void update(Resume r) {
        doUpdate(checkNotExist(r.getUuid()), r);
    }

    public final void save(Resume r) {
        doSave(checkExist(r.getUuid()), r);
    }

    public final Resume get(String uuid) {
        return doGet(checkNotExist(uuid));
    }

    public final void delete(String uuid) {
        doDelete(checkNotExist(uuid));
    }

    protected abstract Object getSearchKey(String searchKey);

    protected abstract void doSave(Object key, Resume r);

    protected abstract void doDelete(Object key);

    protected abstract void doUpdate(Object key, Resume r);

    protected abstract Resume doGet(Object key);

    protected abstract Object checkExist(String uuid);

    protected abstract Object checkNotExist(String uuid);

}
