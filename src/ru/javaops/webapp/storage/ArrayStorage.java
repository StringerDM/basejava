package ru.javaops.webapp.storage;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object getSearchKey(String searchKey) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(searchKey)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected int insertResume(int index) {
        return size;
    }

    @Override
    protected void deleteResume(int index) {
        storage[index] = storage[size];
    }
}
