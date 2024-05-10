package com.example.hkproject.constant;

public enum OrderStatus {
    NONE(0, ""),
    UNASSIGNED(1, "UNASSIGNED"),
    TAKEN(2, "TAKEN");

    private final int value;
    private final String description;

    OrderStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    // 根据整数值获取枚举值
    public static OrderStatus fromValue(Integer value) {
        if (value == null) {
            return OrderStatus.NONE;
        }
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus value: " + value);
    }

    // 根据描述获取枚举值
    public static OrderStatus fromDescription(String description) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus description: " + description);
    }
}
