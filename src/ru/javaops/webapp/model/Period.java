package ru.javaops.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {
    private final LocalDate start;
    private final LocalDate end;
    private final String title;
    private final String description;

    public Period(LocalDate start, LocalDate end, String title, String description) {
        Objects.requireNonNull(start, "organizations must not be null");
        Objects.requireNonNull(end, "organizations must not be null");
        Objects.requireNonNull(title, "organizations must not be null");
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "\n" + start + " - " + end + "\t" + title + "\n" + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!start.equals(period.start)) return false;
        if (!end.equals(period.end)) return false;
        if (!title.equals(period.title)) return false;
        return Objects.equals(description, period.description);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
