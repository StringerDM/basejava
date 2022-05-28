package ru.javaops.webapp.model;

import java.util.List;

public class Organisation {
    private final String title;
    private final String website;
    private final List<Period> periods;

    public Organisation(String title, String website, List<Period> periods) {
        this.title = title;
        this.website = website;
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "\n" + title + "\n" + website + "\n" + periods;
    }
}
