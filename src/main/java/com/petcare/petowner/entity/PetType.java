package com.petcare.petowner.entity;

import java.util.Arrays;

public enum PetType {
    DOG,
    CAT,
    SNAKE,
    SPIDER,
    OTHER;

    public static PetType fromString(String type) {
        return Arrays.stream(PetType.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid pet type: " + type));
    }

    // Convert from Enum to String
    public String toStringValue() {
        return this.name();
    }
}

