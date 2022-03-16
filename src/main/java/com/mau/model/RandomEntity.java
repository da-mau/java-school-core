package com.mau.model;

public class RandomEntity {
    private int id;
    private String name;

    public RandomEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RandomEntity() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
