package ru.javaops.webapp.model;

import java.time.LocalDate;

public class Period {
    public LocalDate start;
    public LocalDate end;
    public String title;
    public String description;

    public Period(LocalDate start, LocalDate end, String title, String description) {
        this.start = start;
        this.end = end;
        this.title = title;
        if (description != null) {
            this.description = description;
        } else {
            this.description = "";
        }

    }

    @Override
    public String toString() {
        return "\n" + start + " - " + end + "\t" + title + "\n" + description;
    }
}
