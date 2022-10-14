package net.cavitos.documentor.domain.model.status;

public enum ActiveStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    ActiveStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ActiveStatus of(final String value) {

        return value.equalsIgnoreCase("Active") ? ACTIVE : INACTIVE;
    }
}
