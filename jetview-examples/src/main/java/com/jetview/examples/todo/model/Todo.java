package com.jetview.examples.todo.model;

import java.io.Serializable;
import java.util.UUID;

public class Todo implements Serializable {

    private final String id;
    private String title;
    boolean completed;

    public Todo(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
