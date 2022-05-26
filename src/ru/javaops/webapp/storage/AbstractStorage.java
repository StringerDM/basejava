package ru.javaops.webapp.storage;

import ru.javaops.webapp.exception.ExistStorageException;
import ru.javaops.webapp.exception.NotExistStorageException;
import ru.javaops.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {
    protected final static Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName)
            .thenComparing(Resume::getUuid);

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
        List<Resume> allResumes = doCopyAll();
        allResumes.sort(RESUME_COMPARATOR);
        return allResumes;
    }

    protected abstract SK getSearchKey(String uuid);

    protected abstract void doSave(SK searchKey, Resume r);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Resume r);

    protected abstract Resume doGet(SK searchKey);

    protected abstract List<Resume> doCopyAll();

    protected SK getNotExistKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected SK getExistKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract boolean isExist(SK searchKey);

}
