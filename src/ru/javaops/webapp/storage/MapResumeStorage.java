package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object getSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid);
        }
        return null;
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        Resume resume = (Resume) searchKey;
        storage.remove(resume.getUuid());
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        Resume resume = (Resume) searchKey;
        doSave(resume.getUuid(), r);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        Resume resume = (Resume) searchKey;
        return storage.get(resume.getUuid());
    }

    @Override
    protected List<Resume> getList() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsValue((Resume) searchKey);
    }
}
