package com.jetview.examples.todo;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.IEventHandler;
import com.jetview.examples.todo.model.Todo;

/**
 * @author Roman Kochergin
 */
@View("templates/todo/TodoListItem.peb")
public class TodoListItem extends Component {

    private Todo todo;

    public TodoListItem(Todo todo) {
        this.todo = todo;
        setProperty("todo", () -> this.todo);
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
        notifyStateChange();
    }

    public void setCompleteHandler(IEventHandler completeHandler) {
        setListener("onComplete", completeHandler);
    }

    public void setChangeHandler(IEventHandler changeHandler) {
        setListener("onChange", changeHandler);
    }

    public void setDeleteHandler(IEventHandler deleteHandler) {
        setListener("onDelete", deleteHandler);
    }
}
