package com.example.kamil_2.liteapp;

/**
 * Created by Kamil_2 on 2016-12-09.
 */

public class TodoTask {
    private long id;
    private String name;
    private String description;

    public TodoTask(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}