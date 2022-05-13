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
            int removedIndex = 0;
            for (int i = 0; i < size; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    isRemoved = true;
                    removedIndex = i;
                }
            }
            if(isRemoved) {
                System.arraycopy(storage, removedIndex + 1, storage, removedIndex, size - removedIndex);
                size--;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }
}
