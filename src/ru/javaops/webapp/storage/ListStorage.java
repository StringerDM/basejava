package ru.javaops.webapp.storage;

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
    protected int getIndex(String uuid) {
        Resume resume = null;
        for (Resume r : storage) {
            if (uuid.equals(r.getUuid())) {
                resume = r;
                break;
            }
        }
        return storage.indexOf(resume);
    }

    @Override
    protected void doSave(Resume r, int index) {
        storage.add(r);
    }

    @Override
    protected void doDelete(int index) {
        storage.remove(index);
    }

    @Override
    protected void replaceItem(int index, Resume r) {
        doDelete(index);
        doSave(r, 0);
    }

    @Override
    protected void checkOverflow(Resume r) {
    }

    @Override
    protected Resume getResume(int index) {
        return storage.get(index);
    }
}
