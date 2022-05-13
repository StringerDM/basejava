import java.util.Arrays;

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
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
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
            if(isRemoved) {
                size--;
            }
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        if (size != 0) {
            return Arrays.copyOf(storage, size);
        } else {
            return new Resume[0];
        }
    }

    int size() {
        return size;
    }
}
