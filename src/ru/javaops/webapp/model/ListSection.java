package ru.javaops.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private List<String> strings = new ArrayList<>();

    public ListSection() {
    }

    public ListSection(String... items) {
        this(Arrays.asList(items));
    }

    public ListSection(List<String> strings) {
        Objects.requireNonNull(strings, "items must not be null");
        this.strings = strings;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void addString(String string) {
        this.strings.add(string);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        strings.forEach(s -> str.append(s).append("\n"));
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return strings.equals(that.strings);
    }

    @Override
    public int hashCode() {
        return strings.hashCode();
    }
}