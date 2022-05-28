package ru.javaops.webapp.model;

import java.util.List;

public class ListSection extends AbstractSection {
    private final List<String> strings;

    public ListSection(List<String> strings) {
        this.strings = strings;
    }

    @Override
    public String toString() {
        return strings.toString();
    }
}
