package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    protected final static Comparator<Resume> RESUME_UUID_COMPARATOR = (r1, r2) -> r1.getUuid().compareTo(r2.getUuid());
    protected final static Comparator<Resume> RESUME_NAME_COMPARATOR = (r1, r2) -> r1.getFullName().compareTo(r2.getFullName());


    @Override
    public final void update(Resume r) {
        doUpdate(getExistKey(r.getUuid()), r);
    }

    @Override
    public final void save(Resume r) {
        doSave(getNotExistKey(r.getUuid()), r);
    }

    @Override
    public final Resume get(String uuid) {
        return doGet(getExistKey(uuid));
    }

    @Override
    public final void delete(String uuid) {
        doDelete(getExistKey(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> allResumes = getList();
        allResumes.sort(RESUME_NAME_COMPARATOR.thenComparing(RESUME_UUID_COMPARATOR));
        return allResumes;
    }

    protected abstract List<Resume> getList();

    protected abstract Object getSearchKey(String uuid);

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
