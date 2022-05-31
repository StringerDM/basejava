package ru.javaops.webapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.javaops.webapp.util.DateUtil.NOW;
import static ru.javaops.webapp.util.DateUtil.of;

public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Link homePage;
    private final List<Period> periods;

    public Organization(String name, String url, Period... periods) {
        this(new Link(name, url), Arrays.asList(periods));
    }

    public Organization(String name, String url, List<Period> periods) {
        this(new Link(name, url), periods);
    }

    public Organization(Link homePage, List<Period> periods) {
        this.homePage = homePage;
        this.periods = periods;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return homePage + "\n" + periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!homePage.equals(that.homePage)) return false;
        return periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        int result = homePage.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    public static class Period implements Serializable {

        private final LocalDate start;
        private final LocalDate end;
        private final String title;
        private final String description;

        public Period(int starYear, Month startMonth, String title, String description) {
            this(of(starYear, startMonth), NOW, title, description);
        }

        public Period(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

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
}
