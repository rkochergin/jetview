package com.jetview.examples.todo.service;

import com.jetview.examples.todo.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoService {

    private static final List<Todo> TODO_LIST = new ArrayList<>();

    static {
        TODO_LIST.add(new Todo("Learn Java"));
        TODO_LIST.add(new Todo("Kill Kenny"));
    }

    public List<Todo> getList() {
        return List.copyOf(TODO_LIST);
    }

    public Todo get(String id) {
        return TODO_LIST.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public int getCount() {
        return TODO_LIST.size();
    }

    public int getCompletedCount() {
        return (int) TODO_LIST.stream()
                .filter(Todo::isCompleted)
                .count();
    }

    public boolean remove(String id) {
        return TODO_LIST.removeIf(todo -> todo.getId().equals(id));
    }

    public void add(Todo todo) {
        TODO_LIST.add(todo);
    }
}
