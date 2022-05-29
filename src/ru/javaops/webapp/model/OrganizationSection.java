package ru.javaops.webapp.model;

import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private final List<Organization> organisations;

    public OrganizationSection(List<Organization> organisations) {
        this.organisations = organisations;
    }

    public List<Organization> getOrganisations() {
        return organisations;
    }

    @Override
    public String toString() {
        return organisations.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

        return Objects.equals(organisations, that.organisations);
    }

    @Override
    public int hashCode() {
        return organisations != null ? organisations.hashCode() : 0;
    }
}
