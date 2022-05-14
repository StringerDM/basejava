package ru.javaops.webapp.model;

/**
 * Initial resume class
 */
public class Resume {

    // Unique identifier
    private String getUuid;

    @Override
    public String toString() {
        return getUuid;
    }

    public String getUuid() {
        return getUuid;
    }

    public void setUuid(String getUuid) {
        this.getUuid = getUuid;
    }
}
