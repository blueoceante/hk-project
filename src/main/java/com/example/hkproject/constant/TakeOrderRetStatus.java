package com.example.hkproject.constant;

public enum TakeOrderRetStatus {
    SUCCESS(0, "SUCCESS"),
    ORDERNOTEXIST(1, "Order does not exist"),
    TAKEINGBYUSER(2, "Order is being processed by another user, please try again later"),
    PROCESSED(3, "Order has already been processed"),
    ERROR(4, "Something went wrong, please try again");

    private final int value;
    private final String description;

    TakeOrderRetStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
