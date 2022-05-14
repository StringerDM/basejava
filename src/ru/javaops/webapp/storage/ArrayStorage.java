package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    private final Resume[] storage = new Resume[10000];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        int index = check(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("Error: невозможно обновить, резюме " + resume.getUuid() + " не найдено");
        }
    }

    public void save(Resume resume) {
        if (size > storage.length) {
            System.out.println("Error: невозможно добавить резюме, хранилище переполнено");
            return;
        }
        int index = check(resume.getUuid());
        if (index < 0) {
            storage[size] = resume;
            size++;
        } else {
            System.out.println("Error: невозможно добавить, резюме " + resume.getUuid() + " существует");
        }
    }

    public Resume get(String uuid) {
        int index = check(uuid);
        if (index < 0) {
            System.out.println("Error: невозможно получить, резюме " + uuid + " не найдено");
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = check(uuid);
        if (index >= 0) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
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

    public int size() {
        return size;
    }

    private int check(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
