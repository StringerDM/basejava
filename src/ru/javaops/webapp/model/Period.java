package ru.javaops.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Period {
    private final LocalDate start;
    private final LocalDate end;
    private final String title;
    private final String description;

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

        if (!Objects.equals(start, period.start)) return false;
        if (!Objects.equals(end, period.end)) return false;
        if (!Objects.equals(title, period.title)) return false;
        return Objects.equals(description, period.description);
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + description.hashCode();
        return result;
    }
}
