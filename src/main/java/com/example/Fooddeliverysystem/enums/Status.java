package com.example.Fooddeliverysystem.enums;

public enum Status {
    INACTIVE(1),
    ACTIVE(2),
    DELETED(3),
    BANNED(4);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}