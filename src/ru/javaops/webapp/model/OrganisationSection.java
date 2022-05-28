package ru.javaops.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class OrganisationSection extends AbstractSection {
    public List<Organisation> organisations = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Organisation org : organisations) {
            stringBuilder.append(org).append("\n");
        }
        return stringBuilder.toString();
    }
}
