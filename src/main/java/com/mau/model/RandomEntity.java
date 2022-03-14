package com.mau.model;

public class RandomEntity {
    private final int id;
    private final String name;

    public RandomEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
