package ru.javaops.webapp.storage;

import ru.javaops.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object getSearchKey(String searchKey) {
        Resume r = new Resume(searchKey);
        return Arrays.binarySearch(storage, 0, size, r);
    }

    @Override
    protected int insertResume(int index) {
        index = -index - 1;
        System.arraycopy(storage, index, storage, index + 1, size - index);
        return index;
    }

    @Override
    protected void deleteResume(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index);
    }
}
