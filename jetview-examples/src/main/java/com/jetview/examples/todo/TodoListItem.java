package com.jetview.examples.todo;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.AjaxBehavior;
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
        addValue("todo", () -> this.todo);
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
        notifyStateChange();
    }

    public void setCompleteHandler(IEventHandler completeHandler) {
        addBehavior(new AjaxBehavior("onComplete", completeHandler));
    }

    public void setChangeHandler(IEventHandler changeHandler) {
        addBehavior(new AjaxBehavior("onChange", changeHandler));
    }

    public void setDeleteHandler(IEventHandler deleteHandler) {
        addBehavior(new AjaxBehavior("onDelete", deleteHandler));
    }
}
