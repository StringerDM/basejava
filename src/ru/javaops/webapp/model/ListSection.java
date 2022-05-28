package ru.javaops.webapp.model;

import java.util.Arrays;
import java.util.List;

public class ListSection extends AbstractSection {
    public List<String> strings;

    public ListSection(String... strings) {
        this.strings = Arrays.asList(strings);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strings) {
            stringBuilder.append(s).append("\n");
        }
        return stringBuilder.toString();
    }
}
