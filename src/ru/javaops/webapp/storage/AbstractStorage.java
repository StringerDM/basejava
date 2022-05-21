package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public final void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            replaceItem(index, r);
        } else {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    public final void save(Resume r) {
        int index = getIndex(r.getUuid());
        checkOverflow(r);
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        } else {
            doSave(r, index);
        }
    }

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getResume(index);
    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            doDelete(index);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract int getIndex(String uuid);

    protected abstract void doSave(Resume r, int index);

    protected abstract void doDelete(int index);

    protected abstract void replaceItem(int index, Resume r);

    protected abstract void checkOverflow(Resume r);

    protected abstract Resume getResume(int index);

}
