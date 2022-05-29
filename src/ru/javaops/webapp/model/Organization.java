package ru.javaops.webapp.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private final Link homePage;
    private final List<Period> periods;

    public Organization(String name, String url, List<Period> periods) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(periods, "periods must not be null");
        this.homePage = new Link(name, url);
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
}
