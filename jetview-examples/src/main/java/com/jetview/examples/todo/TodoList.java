package com.jetview.examples.todo;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Composite;
import com.jetview.core.component.Container;
import com.jetview.examples.todo.model.Todo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Roman Kochergin
 */
@View("templates/todo/TodoList.peb")
public class TodoList extends Container {

    private final Composite<TodoListItem> todoListItems;

    private TodoCompleteHandler todoCompleteHandler;
    private TodoChangeHandler todoChangeHandler;
    private TodoDeleteHandler todoDeleteHandler;

    public TodoList(List<Todo> todos) {
        todoListItems = new Composite<>(todos.stream()
                .map(this::createTodoListItem)
                .toList());
        addComponent("items", todoListItems);
    }

    public void addTodo(Todo todo) {
        todoListItems.add(createTodoListItem(todo));
        notifyStateChange();
    }

    public void updateTodo(Todo todo) {
        todoListItems.getChildren()
                .filter(item -> item.getTodo().getId().equals(todo.getId()))
                .findFirst()
                .ifPresent(item -> item.setTodo(todo));
    }

    public void removeTodo(String todoId) {
        var removed = todoListItems.removeIf(item ->
                item.getTodo().getId().equals(todoId));
        if (removed) {
            notifyStateChange();
        }
    }

    private TodoListItem createTodoListItem(Todo todo) {
        var item = new TodoListItem(todo);
        item.setCompleteHandler(event -> {
            if (todoCompleteHandler != null) {
                var completed = event.getParam("completed", Boolean.class);
                todoCompleteHandler.onComplete(todo.getId(), completed);
            }
        });
        item.setChangeHandler(event -> {
            if (todoChangeHandler != null) {
                var title = event.getParam("title", String.class);
                todoChangeHandler.onChange(todo.getId(), title);
            }
        });
        item.setDeleteHandler(event -> {
            if (todoDeleteHandler != null) {
                todoDeleteHandler.onDelete(todo.getId());
            }
        });
        return item;
    }

    public void setTodoCompleteHandler(TodoCompleteHandler completeHandler) {
        this.todoCompleteHandler = completeHandler;
    }

    public void setTodoChangeHandler(TodoChangeHandler changeHandler) {
        this.todoChangeHandler = changeHandler;
    }

    public void setTodoDeleteHandler(TodoDeleteHandler deleteHandler) {
        this.todoDeleteHandler = deleteHandler;
    }

    @FunctionalInterface
    public interface TodoCompleteHandler extends Serializable {
        void onComplete(String id, boolean completed);
    }

    @FunctionalInterface
    public interface TodoChangeHandler extends Serializable {
        void onChange(String id, String title);
    }

    @FunctionalInterface
    public interface TodoDeleteHandler extends Serializable {
        void onDelete(String id);
    }
}
