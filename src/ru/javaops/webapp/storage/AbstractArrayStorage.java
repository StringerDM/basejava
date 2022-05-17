package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public final void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            storage[index] = r;
        } else {
            System.out.println("Resume " + r.getUuid() + " not found");
        }
    }

    public final void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (size >= STORAGE_LIMIT) {
            System.out.println("Unable to add resume " + r.getUuid() + ", storage is full");
        } else if (index >= 0) {
            System.out.println("Resume " + r.getUuid() + " already exist");
        } else {
            insertResume(r, index);
            size++;
        }
    }

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Resume " + uuid + " not exist");
            return null;
        }
        return storage[index];
    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            size--;
            withdrawResume(index);
        } else {
            System.out.println("Unable to delete, resume " + uuid + " no found");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insertResume(Resume r, int index);

    protected abstract void withdrawResume(int index);
}
