package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    protected static final int MAX_SIZE = 10000;
    private int size = 0;
    private final Resume[] storage = new Resume[MAX_SIZE];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("Error: невозможно обновить, резюме " + resume.getUuid() + " не найдено");
        }
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (size >= MAX_SIZE) {
            System.out.println("Error: невозможно добавить резюме, хранилище переполнено");
        } else if (index >= 0) {
            System.out.println("Error: невозможно добавить, резюме " + resume.getUuid() + " существует");
        } else {
            storage[size] = resume;
            size++;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            size--;
            storage[index] = storage[size];
            storage[size] = null;

        } else {
            System.out.println("Error: невозможно удалить, резюме " + uuid + " не найдено");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
