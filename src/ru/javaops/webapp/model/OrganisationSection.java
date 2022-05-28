package ru.javaops.webapp.model;

import java.util.List;

public class OrganisationSection extends AbstractSection {
    private final List<Organisation> organisations;

    public OrganisationSection(List<Organisation> organisations) {
        this.organisations = organisations;
    }

    @Override
    public String toString() {
        return organisations.toString();
    }
}
