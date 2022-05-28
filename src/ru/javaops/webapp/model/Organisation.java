package ru.javaops.webapp.model;

import java.util.Arrays;
import java.util.List;

public class Organisation {
    public String title;
    public String website;
    public List<Period> periods;

    public Organisation(String title, String website, Period... periods) {
        this.title = title;
        this.website = website;
        this.periods = Arrays.asList(periods);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("\n" + title + "\n" + website + "\n");
        for (Period period : periods) {
            stringBuilder.append(period.toString());
        }
        return stringBuilder.toString();
    }
}
