package com.jetview.examples.todo;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;
import com.jetview.examples.todo.model.Todo;
import com.jetview.examples.todo.service.TodoService;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/todo/TodoPage.peb")
public class TodoPage extends Page {

    public TodoPage() {
        var todoService = getResource(TodoService.class);
        var counter = new Counter(todoService.getCompletedCount(), todoService.getCount());
        var todoList = createTodoList(todoService, counter);
        var form = new AddForm(title -> {
            var todo = new Todo(title);
            todoService.add(todo);
            counter.setTotalCount(todoService.getCount());
            todoList.addTodo(todoService.get(todo.getId()));
        });
        addComponent("counter", counter);
        addComponent("addForm", form);
        addComponent("todoList", todoList);
    }

    private static TodoList createTodoList(TodoService todoService, Counter counter) {
        var todoList = new TodoList(todoService.getList());
        todoList.setTodoCompleteHandler((id, completed) -> {
            var todo = todoService.get(id);
            if (todo.isCompleted() != completed) {
                todo.setCompleted(completed);
                counter.setCompletedCount(todoService.getCompletedCount());
                todoList.updateTodo(todo);
            }
        });
        todoList.setTodoChangeHandler((id, title) -> {
            var todo = todoService.get(id);
            if (!todo.getTitle().equals(title)) {
                todo.setTitle(title);
                todoList.updateTodo(todo);
            }
        });
        todoList.setTodoDeleteHandler(id -> {
            if (todoService.remove(id)) {
                counter.setTotalCount(todoService.getCount());
                counter.setCompletedCount(todoService.getCompletedCount());
                todoList.removeTodo(id);
            }
        });
        return todoList;
    }
}
