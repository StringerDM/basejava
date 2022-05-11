/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size = 0;
    private final Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        Resume[] tempStorage = getAll();
        Resume resume = new Resume();
        for (int i = 0; i < tempStorage.length; i++) {
            if (storage[i].uuid.equals(uuid)) {
                resume = storage[i];
                break;
            }
        }
        return resume;
    }

    void delete(String uuid) {
        if (size > 0) {
            boolean isRemoved = false;
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    storage[i] = null;
                    isRemoved = true;
                }
                if (isRemoved) {
                    if (i < size - 1) {
                        storage[i] = storage[i + 1];
                    } else {
                        storage[i] = null;
                    }
                }
            }
            size--;
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        if (size != 0) {
            Resume[] tempStorage = new Resume[size];
            System.arraycopy(storage, 0, tempStorage, 0, tempStorage.length);
            return tempStorage;
        } else {
            return new Resume[0];
        }
    }

    int size() {
        return size;
    }
}
