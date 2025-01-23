package com.jptest.enums;

public enum UserStatus {
    ACTIVE(1, "Active"),
    DELETED(0, "Deleted");

    private final int value;
    private final String label;

    UserStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static UserStatus fromValue(int value) {
        for (UserStatus status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value for UserStatus: " + value);
    }

    public static UserStatus fromLabel(String label) {
        for (UserStatus status : UserStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown label: " + label);
    }
}
