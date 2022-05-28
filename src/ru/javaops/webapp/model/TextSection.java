package ru.javaops.webapp.model;

public class TextSection extends AbstractSection {
    public String content;

    public TextSection(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
