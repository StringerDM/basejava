package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {


    @Override
    public void save(Resume r) {
        //TODO adapt to sortedArray
        int index = getIndex(r.getUuid());
        if (size >= STORAGE_LIMIT) {
            System.out.println("Error: невозможно добавить резюме, хранилище переполнено");
        } else if (index >= 0) {
            System.out.println("Error: невозможно добавить, резюме " + r.getUuid() + " существует");
        } else {
//            storage[size] = r;

            size++;
        }
    }

    @Override
    public void delete(String uuid) {
        //TODO adapt to sortedArray

        int index = getIndex(uuid);
        if (index >= 0) {
//            size--;
//            storage[index] = storage[size];
//            storage[size] = null;

        } else {
            System.out.println("Error: невозможно удалить, резюме " + uuid + " не найдено");
        }

    }

    @Override
    public Resume[] getAll() {
        return new Resume[0];
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
